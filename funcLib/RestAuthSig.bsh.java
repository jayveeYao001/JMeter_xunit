import java.text.*;
import java.util.Date;
import MD5.Str2Md5;
import base64.Str2Base64;

Date currentTime = new Date();
SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMddHHmmss");

String dateString1 = formatter1.format(currentTime);
String acti0_SId = vars.get("V.l_sid"); 
String acti0_Token = vars.get("V.l_token"); 

vars.put("V.l_sig",new Str2Md5().MD5(acti0_SId+acti0_Token+dateString1).toUpperCase()); 
vars.put("V.l_auth",new Str2Base64().encode(acti0_SId+":"+dateString1)); 
//log.info("\n"+ vars.get("V.s_Step") + ": (sig,auth) = (" + vars.get("V.l_sig") + ", " + vars.get("V.l_auth") + ")");