log.info("\n"+ vars.get("V.s_CasePath")+ ") SqlLine1Get<#.join('~')>"+ vars.get("V.s_Keyx")+ ": "+ vars.get("V.s_Step"));

// resuls.split("\n")[1].split('\t').join('~')
var resuls = prev.getResponseDataAsString();
var sqlLine1Split = resuls.split("\n")[1].split('\t');
vars.put("V.l_f_SqlLine1", sqlLine1Split.join('~'));