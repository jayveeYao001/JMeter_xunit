log.info("\n"+ vars.get("V.s_CasePath")+ ") "+ vars.get("V.s_Keyx")+ ": AutoClean * in l_*: "+ vars.get("V.s_Step"));

// ArrayList chkFullLs = new ArrayList();
// String[] chkLs = vars.get("V.l_CHK").split(",");
// chkFullLs.add("V.l_CHK");
// for (String i: chkLs) {
	// chkFullLs.add("V.l_e_"+i.trim());
	// chkFullLs.add("V.l_f_"+i.trim());
// }
// log.info(chkFullLs.toString());

Iterator varsIter = vars.getIterator();
ArrayList names = new ArrayList();
while (varsIter.hasNext()) {
	Object obj = varsIter.next();
	String key = obj.getKey();
    // if (key.startsWith("V.l_") && !chkFullLs.contains(key))
	if (key.startsWith("V.l_")) {
        names.add(key);
	}
}
for (int i=0; i<names.size(); i++) {
	// log.info("remove:"+names.get(i));
	vars.remove(names.get(i));
}