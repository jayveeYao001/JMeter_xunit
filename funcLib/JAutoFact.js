log.info("\nJAutoFactInLs: Parameters="+Parameters);
var g_null = vars.get("DB.G_NULL");
var parseObj = JSON.parse(Parameters);
var jData = prev.getResponseDataAsString().trim();
try {
	jObj = JSON.parse(jData);
	var sonObj = jObj;
	for (var i=0; i<parseObj.sonPath.length; i++) {
		var sonKey = parseObj.sonPath[i];
//		log.info("sonKey="+sonKey);
		sonObj = sonObj[sonKey];
//		log.info("sonObj="+JSON.stringify(sonObj));
	}
} catch(err) {
	sonObj = null;
	throw "\nJAutoFactInLs: error at sonObj!!! Parameters="+Parameters+", err="+err;
}

var findObj = sonObj;
var findAt=4444; //不需要find PASS
if (Object.prototype.toString.call(parseObj.key)=="[object String]"
	&& Object.prototype.toString.call(sonObj)=="[object Array]") {  // sonObj isa Array<Object>
	var findAt=-1;
	for (var i=0; i<sonObj.length; i++) {
		var element = sonObj[i];
		if (Object.prototype.toString.call(element)!="[object Object]") {break;}
//		log.info("i="+i+", element="+JSON.stringify(element)+", get="+element[parseObj.key]+", expe="+parseObj.expe);
		if ((element[parseObj.key]==parseObj.expe) == parseObj.ifEeq) {
			findAt=i;
			findObj=element;
			break;
		}
	}
	vars.put("V.l_f_SEQ", findAt);
}

if (findAt==-1) {
	log.info("JAutoFactInLs: fail at: sonObj find !!! Parameters="+Parameters);
} else {
	for (key in sonObj) {
		var keyFact = "V.l_f_"+key;
		var old = vars.get(keyFact);
		if (old!=null && old!=g_null) {continue;}  // jump when key has value(and value!=g_null)
		var val = sonObj[key];
		if (Object.prototype.toString.call(val)!="[object String]") (val=JSON.stringify(val))
		vars.put(keyFact, val);
	}
}
