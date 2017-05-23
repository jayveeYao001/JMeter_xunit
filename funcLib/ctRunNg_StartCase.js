
String.prototype.trim=function(){return this.replace(/(^\s+)|(\s+$)/g,'')}


// Del CaseLevelVar, V._* and V.bk_*
var varsIter = vars.getIterator();
var names = [];
while (varsIter.hasNext()) {
	var obj = varsIter.next();
	var key = obj.getKey();
	if (key.indexOf("V._")==0 || key.indexOf("V.bk_")==0) {
        names.push(key);
	}
}
for (var i=0; i<names.length; i++) {
	vars.remove(names[i]);
}


// Create: s_CasePath and V._*
var caseName = vars.get("V.s_Case").trim();
var caseFlagAt = caseName.indexOf(")");
vars.put("V.s_CasePath", vars.get("V.s_ItemPath")+"."+vars.get("V.s_CaseId"));  // like "3.2"

for (var i=0; i<=9; i++) {
	var ivar = "V."+vars.get("V.s_n"+i.toString());   
	vars.put(ivar, vars.get("V.s_v"+i.toString()));
}