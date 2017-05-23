log.info("\n"+ vars.get("V.s_CasePath")+ ") "+ vars.get("V.s_Keyx")+ ": AutoChk f_* in l_CHK, then AutoClean: "+ vars.get("V.s_Step"));
// log.info("\n    // f=V.l_f_i,e=V.l_e_i FOR i IN 'V.l_CHK_*': fail at: e!=G_SKIP and ((l_FACTMUST and f==null) or (e!=null and f!=e))");
String s__lastChkLog = "    //"+ vars.get("V.s_CasePath")+ ") "+ vars.get("V.s_Keyx");

String g_null = vars.get("DB.G_NULL");
String g_skip = vars.get("DB.G_SKIP");
String g_false =vars.get("DB.G_FALSE");
String factMust = vars.get("V.l_FACTMUST");
String delLocal = vars.get("V.l_CHKONCE");
Boolean factMustFlag = true;
if (factMust!=null && factMust.equals(g_false)) {factMustFlag = false;}
if (vars.get("V.l_CHK")==null) {
    ATErrorAt_null_CHK("!!!! V.l_CHK is null!");
}

// chk: V.l_MUST* and V.bk_MUST*
Iterator varsIter = vars.getIterator();
ArrayList mustKeysLocal = new ArrayList();
ArrayList mustKeysBizkeyw = new ArrayList();
while (varsIter.hasNext()) {
    Object obj = varsIter.next();
    String key = obj.getKey();
    if (key.startsWith("V.l_MUST")) {
        String[] mustLs = vars.get(key).split(",");
        for (String i: mustLs) {
            mustKeysLocal.add(i.trim());
        }
    }
    if (key.startsWith("V.bk_MUST")) {
        String[] mustLs = vars.get(key).split(",");
        for (String i: mustLs) {
            mustKeysBizkeyw.add(i.trim());
        }
    }
}
String lostMustArgs = "";
for (String mustKey: mustKeysLocal) {
	if (vars.get("V.l_"+mustKey)==null) {
		lostMustArgs = lostMustArgs+ "  V.l_"+ mustKey;
	}
}
for (String mustKey: mustKeysBizkeyw) {
	if (vars.get("V.bk_"+mustKey)==null) {
		lostMustArgs = lostMustArgs+ "  V.bk_"+ mustKey;
	}
}
if (!lostMustArgs.equals("")) {
    log.info("//lostMustArgs:"+lostMustArgs);
    Failure = true;
    FailureMessage = FailureMessage+ "\n    {//lostMustArgs:"+ lostMustArgs+ "}";
}

// chk: V.l_CHK
String[] chkLs = vars.get("V.l_CHK").split(",");
String sucMsg = "";
String skipMsg = "";
// log.info("start: for i in chkLs");
for (String i: chkLs) {
    String chk = i.trim();
    
    // get fact
    String fact = vars.get("V.l_f_"+chk);
    if (fact==null) {   // cannot find f_*
        Failure = true;
        FailureMessage = FailureMessage+ "\n    null at: "+ "V.l_f_"+ chk;
        break;
    }
    fact = fact.trim();
    
    // create: expe,ifEq. pri for e_* is higher
    // l_en_不存在、或"l_e_存在且!=g_skip"时，用l_e
    // (expe=e_*,ifEq='==') if (en_*==null || (e_*!=null && e_*!=g_skip)) else (expe=en_*,ifEq='!=')
    String ifEq = "!=";
    String l_en_ = vars.get("V.l_en_"+chk);
    String l_e_ = vars.get("V.l_e_"+chk);
    String expe = l_en_;
    if ( l_en_==null || (l_e_!=null && !l_e_.equals(g_skip)) ) {
        expe = l_e_;
        ifEq = "==";
    }
    String msg = chk+": (fact="+fact +")  "+ ifEq;
    if (expe==null) {  // diff msg: 'expe=null' and 'expe.equals("null")'
        msg = msg+"  (expe=${java.null}"+");";
    } else {
        msg = msg+"  (expe="+expe +");";
    }
    log.info(msg);
    
    // chk: fact and expe
    if ( expe==null || expe.equals(g_skip) ) {
        skipMsg = skipMsg+ "\n    "+ msg;
        continue;
    }
    // log.info("1");
    if (( factMustFlag && fact.equals(g_null) ) 
        || ( expe!=null && fact.equals(expe)!=ifEq.equals("==") )) {
        // log.info("this check is error.");
        Failure = true;
        FailureMessage = FailureMessage+ "\n    "+ msg;
    // break;
    } else {
        sucMsg = sucMsg+ "\n    "+ msg;
    }
}

if (!skipMsg.equals("")) {
    FailureMessage = FailureMessage+ "\n\n    //skipMsg:"+ skipMsg;
}
if (!sucMsg.equals("")) {
    FailureMessage = FailureMessage+ "\n\n    //sucMsg:"+ sucMsg;
}
log.info((!Failure).toString());
if (Failure) {
    s__lastChkLog = s__lastChkLog+"\n    //failMsg:"+ FailureMessage;
} else {
    s__lastChkLog = s__lastChkLog+ "\n    //skipMsg:"+skipMsg + "\n\n    //sucMsg:"+sucMsg;
}
vars.put("V.s__lastChkLog", s__lastChkLog);

// AutoClean
if (!(delLocal!=null && g_false!=null && delLocal.equals(g_false))) {
    log.info("    AutoChkClean: Del All Locals");
    Iterator varsIter = vars.getIterator();
    ArrayList names = new ArrayList();
    while (varsIter.hasNext()) {
        Object obj = varsIter.next();
        String key = obj.getKey();
        if (key.startsWith("V.l_")) {
            names.add(key);
        }
    }
    for (int i=0; i<names.size(); i++) {
        // log.info("remove:"+names.get(i));
        vars.remove(names.get(i));
    }
} else {
    log.info("    ### AutoChkClean:  Not Del All Locals");
}