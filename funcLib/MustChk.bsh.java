log.info("\n"+ vars.get("V.s_CasePath")+ ") MustChk "+ vars.get("V.s_Keyx")+ ": "+ vars.get("V.s_Step"));

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

// log !Failure
log.info((!Failure).toString());