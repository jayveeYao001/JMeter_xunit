Iterator i = vars.getIterator();

Failure = false;
String dft = vars.get("DB.G_DFT");
log.info("\nAuto Assert V.l_f_* : "+vars.get("V.s_Keyx"));
while (i.hasNext()) {
	Object obj = i.next();
	String key = obj.getKey();
	if (!key.startsWith("V.l_f_") || key.split("_").length!=3) {
		continue;
	}
	String keyPost = key.substring(6);
	String fact = vars.get(key).trim();
	String expe = vars.get("V.l_e_"+keyPost);
	String msg = keyPost+": fact="+fact+", expe="+expe+";";
	log.info(msg);
	if (fact.equals(dft) || (expe!=null && fact.equals(expe)==false)) {
		Failure = true;
		FailureMessage = msg;
		break;
	}
}
log.info((!Failure).toString());