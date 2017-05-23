log.info("\n"+ vars.get("V.s_CasePath")+ ") SqlColumnGet<#toJSON> "+ vars.get("V.s_Keyx")+ ": "+ vars.get("V.s_Step"));

// resuls.split("\n").slice(1) => toJSON
String.prototype.trim=function(){return this.replace(/(^\s+)|(\s+$)/g,'')}
var result = prev.getResponseDataAsString().trim();
var sqlColumn = result.split("\n").slice(1).sort();

var f_SqlColumn = sqlColumn;
if (args.length>0 && args[0]=='parseInt') {
    var f_SqlColumn = [];
    for (var i=0; i<sqlColumn.length; i++) {
        f_SqlColumn[i] = parseInt(sqlColumn[i]);
    }    
}

vars.put("V.l_f_SqlColumn", JSON.stringify(f_SqlColumn));
if (args.length>1) {
	vars.put(args[1], vars.get("V.l_f_SqlColumn"));
	log.info(args[1]+ "="+ vars.get(args[1]));
}