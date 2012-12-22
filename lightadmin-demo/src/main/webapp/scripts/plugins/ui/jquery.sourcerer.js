/*
 Sourcerer Source Viewer v1.1
 http://www.andbeyonddesign.com

 File:    Sourcerer.js
 Author:  Anson Airoldi
 Email:   greatunknown@andbeyonddesign.com
 License: Licensed under MIT
          http://www.opensource.org/licenses/mit-license.php

 Notes:   Requires the latest version of jQuery
          http://code.jquery.com/jquery-1.6.min.js
*/

(function($){ 
      
  // String prototype that wraps the given string with span tags and a class
  // of the color that its contents needs to be
  String.prototype.highlight = function(lang){			
    var Code = this;
		if(this.length){
			for(var i=0; i<lang.length; i++){
				for(var OB in lang[i]){	
					var NeExp = new RegExp(OB, "gi");
					var Pos = Code.search(NeExp);
					var Compos = Code.indexOf('//');
					if(Pos <= Compos || Compos === -1 || Compos === 0){
						Code = Code.replace(NeExp, "<span class='" + lang[i][OB] + "'>$&</span>");
					}
				}
			}
		}else{
			Code = "&nbsp;";
		}
    return Code;
  }	

  // returns an array of regular expressions based on the language
  // that is being highlighted, allows for multiple languages to 
  // be highlighted at once, but also note that some overlapping
  // of colors may occur 
  function GetLang(sets){
	  var l = [];
    if(sets.indexOf('html') !== -1){ l.push(HTMLArr); }
	  if(sets.indexOf('js') !== -1){ l.push(JSArr); }
	  if(sets.indexOf('css') !== -1){ l.push(CSSArr); }
	  if(sets.indexOf('php') !== -1){ l.push(PHPArr); }
	  return (l.length) ? l : false;
  }

  // method that replaces some html entities if not already done
  // this does not prevent the problem with using '<' symbols in
  // javascript as described in the download page at andbeyonddesign.com/Sourcerer
  String.prototype.escapeHTML = function () {                                         
    return(                                                                 
      this.replace(/>/g,'&gt;').                                           
           replace(/</g,'&lt;').                                           
           replace(/&lt;!--\?php/g, '&lt;?php' ).
           replace(/\?--&gt;/g, '?&gt;')                                      
    ); 
  };
 
  // regular expressions for html highlighting
  var HTMLArr = {	
	  "&lt;!--.*--&gt;" : "SC_grey",
	  "&lt;\\/*(div|p|br|hr|link|em|meta|html|h2|h1|h3|ul|!DOCTYPE html|li|ol|body|select|span|head|title|canvas|video|audio|source|pre)\\s*(([A-Za-z]+)=(\"|')[a-zA-Z0-9\\.\\/\\(\\)\\?,&'_#\\s;:%=\\-~]*(\"|')\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_blue",		
	  "&lt;\\/*script\\s*(([A-Za-z]+)=\"[a-zA-Z0-9\\.\\/]*\"\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_red",	
	  "&lt;\\/*a\\s*(([A-Za-z]+)=\"[a-zA-Z0-9\\.\\/\\(\\)\\[\\]\\?\\s\\$=,'_#;:&~(&lt;)(&gt;)-]*\"\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_green",	
	  "&lt;img\\s+(([A-Za-z]+)=\"[a-zA-Z0-9\\.\\/\\(\\),\\s'_#;:]+\"\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_red",	
	  "&lt;\\/*(form|input|textarea|button|option|select)\\s*(([A-Za-z]+)=\"[a-zA-Z0-9\\(\\);,':\\s\\.]*\"\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_gold",
	  "&lt;\\/*(td|tr|table|tbody)\\s*(([A-Za-z]+)=\"[a-zA-Z0-9\\.\\/\\(\\)\\s,'_%#;:~-]*\"\\s*)*\\s*\\/*&gt;(?!.*--&gt;)" : "SC_teal",			
	  "\"[a-zA-Z0-9\\s\\/\\.\\?\\[\\]\\$#;:_',&=%\\)\\(~-]*\"(?!(.*--&gt;)|(\s*[A-Za-z]))" : "SC_navy"	
	};

  // regular expressions for css highlighting
  var CSSArr = 	{ 
	  "((#|\\.)*[a-zA-Z0-9_:,\\s\\.\\*#]+\\s*\\{)|(\\})" : "SC_pink",
    "\\/\\*.*\\*\\/" : "SC_grey",
    "[a-zA-Z0-9-]+:(?![a-zA-Z0-9_:\\s\\.]+\\{)" : "SC_blue",
    "[a-zA-Z0-9,'_#\\(\\)\\/%@\\s\\.\"-]+;" : "SC_green"
  };

  // regular expressions for javascript highlighting			
	var JSArr = { 
    "\\/\\/.*" : "SC_grey",
    "((function|var|this|for|if|else|switch|while|try|catch)\\s*(\\{|\\())" : "SC_blue",			
    "(\\.[a-zA-Z]+(?=\\())|(alert\\()|((String|Date|Array|Number)\\.)" : "SC_teal",
    "(((\"|')[a-zA-Z0-9\\s-\\/\\.\\?#!;:_',&=%\\)\\(~]*(\"|'))(?!(\\>)|(.*--&gt;)|(\s*[A-Za-z])))|(this\\.)|(var\\s)|(return\\s)|(true;)|(false;)|(return;)" : "SC_navy",
    "([\\(\\)\\{\\}\\*\\[\\]\\+-]{1})" : "SC_blue",
    "[0-9]+(?![A-Za-z]+)" : "SC_red",
    "(function|var|this|false|true|return|for|if|else|switch|while|try|catch|do|in)(\\s*[A-Za-z]*\\s*(\\(|\\{))*" : "SC_bold"		
  };

  // regular expressions for php highlighting
  var PHPArr = { 
    "(\\/\\/.*)|(\\/\\*.*\\*\\/)" : "SC_grey",
    "(((\"|')[a-zA-Z0-9\\s-\\/\\.\\?\\*#;:_',&=%\\)\\(~]*(\"|'))(?!\\>))":"SC_navy",
		"(((&lt;\\?php)|(\\?&gt;))(?!.*\\*\\/))|([0-9](?![A_Za-z]*\"|'))" : "SC_red",								
    "(([a-zA-Z0-9_]+\\()|(echo)|(\\$_[a-zA-Z0-9]+))(?!.*\\*\\/)" : "SC_green",
    "[\\(\\)\\{\\}(\\*\\+]{1}(?!.*\\*\\/)" : "SC_blue",
		"\\$[A-Za-z0-9]+" : "SC_teal"
  };
	
  $.fn.extend({ 
    sourcerer: function(settings){
		  var sel = this.selector;
			var def = {
				"lang" : false,
				"numbers" : true,
				"title" : false
			};
			var sets = {};
			
      return this.each(function() {
				if(!$(this).children('.SRC_Wrap').length){
					settings = (typeof(settings) == 'string') ? {"lang" : settings} : settings; 
					$.extend(sets, def, settings);
					var Out = [];
					if(sets.lang){ 		
						var Lines = $(this).html().escapeHTML().split('\n');
						Out.push("<div class='SRC_Wrap'>");
						if(sets.title){ Out.push("<div class='SRC_Title'>"+ sets.title +"</div>"); }
						Out.push("<div class='SRC_NumBox'></div><div class='SRC_CodeBox'>");
						for(var j=0; j<Lines.length; j++){
							Out.push("<div class='SRC_Line'><pre class='SRC_CodeContent'>" + Lines[j].highlight(GetLang(sets.lang)) + "</pre></div>");
						}
					
						Out.push("</div><div style='clear:both;'></div></div>");
						$(this).html(Out.join(''));
						if(sets.numbers){
							var numbox = $(this).find('.SRC_NumBox');
							Out = [];
							for(var j=0; j<Lines.length; j++){
								var h = $($('.SRC_Line')[j]).height();
								Out.push("<div class='SRC_Line' style='height:"+h+"px'><div class='SRC_NumContent'>"+(j+1)+"</div></div>");
							}
							numbox.html(Out.join(''));
						}else{
							$(this).find('.SRC_CodeBox').width('100%');
							$(this).find('.SRC_CodeContent').css('border-left',0);
						}
					}				
					else{
						throw "::No language given or language was invalid for sourcerer call with selector '" + sel + "'";
					}
				}
      });			
    }
  });	
})(jQuery);	
