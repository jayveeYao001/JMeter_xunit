log.info("\n"+ vars.get("V.s_CasePath")+ ") MatchsChkClean "+ vars.get("V.s_Keyx")+ ": "+ vars.get("V.s_Step"));
String s__lastChkLog = "    //"+ vars.get("V.s_CasePath")+ ") "+ vars.get("V.s_Keyx");

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

// chk: V.l_MATCHS
var resuls = SampleResult.getResponseDataAsString();
var matchs = vars.get("V.l_MATCHS");
var chkInfo = "\n    {resuls}={"+ resuls+ "}\n"+ "    {matchs}={"+ matchs+ "}";
if (!resuls.matches(matchs)) {
    Failure = true;
    FailureMessage = FailureMessage+ chkInfo;
}
log.info((!Failure).toString());
if (Failure) {
    s__lastChkLog = s__lastChkLog+"\n    //failMsg:"+ chkInfo;
} else {
    s__lastChkLog = s__lastChkLog+ "\n    //sucMsg:"+ chkInfo;
}
vars.put("V.s__lastChkLog", s__lastChkLog);

// AutoClean
String g_false =vars.get("DB.G_FALSE");
String delLocal = vars.get("V.l_CHKONCE");
if (!(delLocal!=null && g_false!=null && delLocal.equals(g_false))) {
    log.info("    MatchsChkClean: Del All Locals");
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
    log.info("    ### MatchsChkClean:  Not Del All Locals");
}