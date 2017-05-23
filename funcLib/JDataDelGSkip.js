if (vars.get("V.l_data")==null || vars.get("V.l_data")==vars.get("DB.G_NULL")) {
	// log.info("\nJDataDelGSkip");
	
	var g_skip = vars.get("DB.G_SKIP");
	var g_skipFloat = parseFloat(g_skip);
	// log.info("jOld="+Parameters+"; g_skip="+g_skip);
	
    try {
        var jObj = JSON.parse(Parameters);
    } catch(err) {
        throw "\nJDataDelGSKip: error at jObj!!! jData="+Parameters+", err="+err;
    }
    
	for (x in jObj) {
		if (jObj[x]==g_skip || jObj[x]==g_skipFloat) {delete jObj[x];}
	}
	vars.put("V.l_data", JSON.stringify(jObj));
}