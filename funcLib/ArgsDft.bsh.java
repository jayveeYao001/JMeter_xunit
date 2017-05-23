log.info("\n"+ vars.get("V.s_CasePath")+ ") "+ vars.get("V.s_Keyx")+ ": ArgsDft * in l_DFT_* and bk_DFT_* "+ vars.get("V.s_Step"));
// log.info("\n    // V(i)=V.l_DFT_i FOR i=i[8:] IN 'V.l_DFT_*' IF V.l_i==null OR V.l_i==G_DEFAULT");

String g_default = vars.get("DB.G_DEFAULT");

Iterator varsIter = vars.getIterator();
ArrayList dftKeysLocal = new ArrayList();
ArrayList dftKeysBizkeyw = new ArrayList();
while (varsIter.hasNext()) {
    Object obj = varsIter.next();
    String key = obj.getKey();
    if (key.startsWith("V.l_DFT_")) {
        dftKeysLocal.add(key.substring(8));
    }
    if (key.startsWith("V.bk_DFT_")) {
        dftKeysBizkeyw.add(key.substring(9));
    }
}

for (String dftKey: dftKeysLocal) {
	String dftVar = "V.l_"+dftKey;
	String dftVal = "V.l_DFT_"+dftKey;
    String setVal = vars.get(dftVar);
    // log.info("DFT:"+dftVar+"="+setVal);
	if (setVal==null || setVal.equals(g_default)) {
		vars.put(dftVar, vars.get(dftVal));
        // log.info(dftVar + "=" + vars.get(dftVar));
	}
}
for (String dftKey: dftKeysBizkeyw) {
	String dftVar = "V.bk_"+dftKey;
	String dftVal = "V.bk_DFT_"+dftKey;
    String setVal = vars.get(dftVar);
    // log.info("DFT:"+dftVar+"="+setVal);
	if (setVal==null || setVal.equals(g_default)) {
		vars.put(dftVar, vars.get(dftVal));
        // log.info(dftVar + "=" + vars.get(dftVar));
	}
}
