$( function () {

	//===== Autocomplete =====//

	var availableTags = [ "ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++", "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran", "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl", "PHP", "Python", "Ruby", "Scala", "Scheme" ];
	$( "#ac" ).autocomplete( {
								 source: availableTags
							 } );


	//===== Tags =====//

	$( '#tags' ).tagsInput( {width: '100%'} );


	//===== Input limiter =====//

	$( '.limit' ).inputlimiter( {
									limit: 100
									//boxClass: 'limBox',
									//boxAttach: false
								} );


	//===== Masked input =====//

	$.mask.definitions['~'] = "[+-]";
	$( ".maskDate" ).mask( "99/99/9999", {completed: function () {
		alert( "Callback when completed" );
	}} );
	$( ".maskPhone" ).mask( "(999) 999-9999" );
	$( ".maskPhoneExt" ).mask( "(999) 999-9999? x99999" );
	$( ".maskIntPhone" ).mask( "+33 999 999 999" );
	$( ".maskTin" ).mask( "99-9999999" );
	$( ".maskSsn" ).mask( "999-99-9999" );
	$( ".maskProd" ).mask( "a*-999-a999", { placeholder: " " } );
	$( ".maskEye" ).mask( "~9.99 ~9.99 999" );
	$( ".maskPo" ).mask( "PO: aaa-999-***" );
	$( ".maskPct" ).mask( "99%" );


	//===== Placeholder =====//

	$( 'input[placeholder], textarea[placeholder]' ).placeholder();


	//===== Multiple select with dropdown =====//

	$( ".chzn-select" ).chosen();


	//===== Usual validation engine=====//

	$( "#usualValidate" ).validate( {
										rules: {
											firstname: "required",
											minChars: {
												required: true,
												minlength: 3
											},
											maxChars: {
												required: true,
												maxlength: 6
											},
											mini: {
												required: true,
												min: 3
											},
											maxi: {
												required: true,
												max: 6
											},
											range: {
												required: true,
												range: [6, 16]
											},
											emailField: {
												required: true,
												email: true
											},
											urlField: {
												required: true,
												url: true
											},
											dateField: {
												required: true,
												date: true
											},
											digitsOnly: {
												required: true,
												digits: true
											},
											enterPass: {
												required: true,
												minlength: 5
											},
											repeatPass: {
												required: true,
												minlength: 5,
												equalTo: "#enterPass"
											},
											customMessage: "required",


											topic: {
												required: "#newsletter:checked",
												minlength: 2
											},
											agree: "required"
										},
										messages: {
											customMessage: {
												required: "Bazinga! This message is editable"
											},
											agree: "Please accept our policy"
										}
									} );

	//===== Form to Wizard plugin =====//

	$( "#wizForm" ).formwizard( {
									formPluginEnabled: false,
									validationEnabled: true,
									focusFirstInput: false,
									formOptions: {
										success: function ( data ) {
											$( "#status" ).fadeTo( 500, 1, function () {
												$( this ).html( "You are now registered!" ).fadeTo( 5000, 0 );
											} )
										},
										beforeSubmit: function ( data ) {
											$( "#data" ).html( "data sent to the server: " + $.param( data ) );
										},
										dataType: 'json',
										resetForm: true
									},
									disableUIStyles: true,
									validationOptions: {
										rules: {
											req: "required",
											sel: "required",
											chb: "required",
											email: {
												required: true,
												email: true
											}
										},
										messages: {
											req: "This field is required",
											sel: "Oops, required!",
											chb: "Check it",
											email: {
												required: "Please specify your email",
												email: "Correct format is name@domain.com"
											}
										}
									}
								} );


	//===== UI dialog =====//

	$( "#dialog-message" ).dialog( {
									   autoOpen: false,
									   modal: true,
									   buttons: {
										   Ok: function () {
											   $( this ).dialog( "close" );
										   }
									   }
								   } );

	$( "#opener" ).click( function () {
		$( "#dialog-message" ).dialog( "open" );
		return false;
	} );


	//===== PrettyPhoto lightbox plugin =====//

	$( "a[rel^='prettyPhoto']" ).prettyPhoto();


	//===== Dual select boxes =====//

	$.configureBoxes();


	//===== Time picker =====//

	$( '.timepicker' ).timeEntry( {
									  show24Hours: true, // 24 hours format
									  showSeconds: true, // Show seconds?
									  spinnerImage: 'images/ui/spinnerUpDown.png', // Arrows image
									  spinnerSize: [17, 26, 0], // Image size
									  spinnerIncDecOnly: true // Only up and down arrows
								  } );


	//===== File uploader =====//

	$( "#uploader" ).pluploadQueue( {
										runtimes: 'html5,html4',
										url: 'php/upload.php',
										max_file_size: '2mb',
										unique_names: true,
										filters: [
											{title: "Image files", extensions: "jpg,gif,png"},
											{title: "Zip files", extensions: "zip"}
										]
									} );


	//===== File manager =====//

	$( '#fileManager' ).elfinder( {
									  url: 'php/connector.php'
								  } )


	//===== Alert windows =====//

	$( ".bAlert" ).click( function () {
		jAlert( 'This is a custom alert box. Title and this text can be easily editted', 'Alert Dialog Sample' );
	} );

	$( ".bConfirm" ).click( function () {
		jConfirm( 'Can you confirm this?', 'Confirmation Dialog', function ( r ) {
			jAlert( 'Confirmed: ' + r, 'Confirmation Results' );
		} );
	} );

	$( ".bPromt" ).click( function () {
		jPrompt( 'Type something:', 'Prefilled value', 'Prompt Dialog', function ( r ) {
			if ( r ) {
				alert( 'You entered ' + r );
			}
		} );
	} );

	$( ".bHtml" ).click( function () {
		jAlert( 'You can use HTML, such as <strong>bold</strong>, <em>italics</em>, and <u>underline</u>!' );
	} );


	//===== Accordion =====//

	$( 'div.menu_body:eq(0)' ).show();
	$( '.acc .head:eq(0)' ).show().css( {color: "#2B6893"} );

	$( ".acc .head" ).click( function () {
		$( this ).css( {color: "#2B6893"} ).next( "div.menu_body" ).slideToggle( 300 ).siblings( "div.menu_body" ).slideUp( "slow" );
		$( this ).siblings().css( {color: "#404040"} );
	} );


	//===== WYSIWYG editor =====//

	$( '.wysiwyg' ).wysiwyg( {
								 iFrameClass: "wysiwyg-input",
								 controls: {
									 bold: { visible: true },
									 italic: { visible: true },
									 underline: { visible: true },
									 strikeThrough: { visible: false },

									 justifyLeft: { visible: true },
									 justifyCenter: { visible: true },
									 justifyRight: { visible: true },
									 justifyFull: { visible: true },

									 indent: { visible: true },
									 outdent: { visible: true },

									 subscript: { visible: false },
									 superscript: { visible: false },

									 undo: { visible: true },
									 redo: { visible: true },

									 insertOrderedList: { visible: true },
									 insertUnorderedList: { visible: true },
									 insertHorizontalRule: { visible: false },

									 h1: {
										 visible: true,
										 className: 'h1',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h1>' : 'h1',
										 tags: ['h1'],
										 tooltip: 'Header 1'
									 },
									 h2: {
										 visible: true,
										 className: 'h2',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h2>' : 'h2',
										 tags: ['h2'],
										 tooltip: 'Header 2'
									 },
									 h3: {
										 visible: true,
										 className: 'h3',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h3>' : 'h3',
										 tags: ['h3'],
										 tooltip: 'Header 3'
									 },
									 h4: {
										 visible: true,
										 className: 'h4',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h4>' : 'h4',
										 tags: ['h4'],
										 tooltip: 'Header 4'
									 },
									 h5: {
										 visible: true,
										 className: 'h5',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h5>' : 'h5',
										 tags: ['h5'],
										 tooltip: 'Header 5'
									 },
									 h6: {
										 visible: true,
										 className: 'h6',
										 command: ($.browser.msie || $.browser.safari) ? 'formatBlock' : 'heading',
										 arguments: ($.browser.msie || $.browser.safari) ? '<h6>' : 'h6',
										 tags: ['h6'],
										 tooltip: 'Header 6'
									 },

									 cut: { visible: true },
									 copy: { visible: true },
									 paste: { visible: true },
									 html: { visible: true },
									 increaseFontSize: { visible: false },
									 decreaseFontSize: { visible: false }
								 },
								 events: {
									 click: function ( event ) {
										 if ( $( "#click-inform:checked" ).length > 0 ) {
											 event.preventDefault();
											 alert( "You have clicked jWysiwyg content!" );
										 }
									 }
								 }
							 } );

	//$('.wysiwyg').wysiwyg("insertHtml", "Sample code");


	//===== ToTop =====//

	$().UItoTop( { easingType: 'easeOutQuart' } );


	//===== Spinner options =====//

	var itemList = [
		{url: "http://ejohn.org", title: "John Resig"},
		{url: "http://bassistance.de/", title: "J&ouml;rn Zaefferer"},
		{url: "http://snook.ca/jonathan/", title: "Jonathan Snook"},
		{url: "http://rdworth.org/", title: "Richard Worth"},
		{url: "http://www.paulbakaus.com/", title: "Paul Bakaus"},
		{url: "http://www.yehudakatz.com/", title: "Yehuda Katz"},
		{url: "http://www.azarask.in/", title: "Aza Raskin"},
		{url: "http://www.karlswedberg.com/", title: "Karl Swedberg"},
		{url: "http://scottjehl.com/", title: "Scott Jehl"},
		{url: "http://jdsharp.us/", title: "Jonathan Sharp"},
		{url: "http://www.kevinhoyt.org/", title: "Kevin Hoyt"},
		{url: "http://www.codylindley.com/", title: "Cody Lindley"},
		{url: "http://malsup.com/jquery/", title: "Mike Alsup"}
	];

	var opts = {
		's1': {decimals: 2},
		's2': {stepping: 0.25},
		's3': {currency: '$'},
		's4': {},
		's5': {
			//
			// Two methods of adding external items to the spinner
			//
			// method 1: on initalisation call the add method directly and format html manually
			init: function ( e, ui ) {
				for ( var i = 0; i < itemList.length; i++ ) {
					ui.add( '<a href="' + itemList[i].url + '" target="_blank">' + itemList[i].title + '</a>' );
				}
			},

			// method 2: use the format and items options in combination
			format: '<a href="%(url)" target="_blank">%(title)</a>',
			items: itemList
		}
	};

	for ( var n in opts ) {
		$( "#" + n ).spinner( opts[n] );
	}

	$( "button" ).click( function ( e ) {
		var ns = $( this ).attr( 'id' ).match( /(s\d)\-(\w+)$/ );
		if ( ns != null ) {
			$( '#' + ns[1] ).spinner( (ns[2] == 'create') ? opts[ns[1]] : ns[2] );
		}
	} );


	//===== Contacts list =====//

	$( '#myList' ).listnav( {
								initLetter: 'a',
								includeAll: true,
								includeOther: true,
								flagDisabled: true,
								noMatchText: 'Nothing matched your filter, please click another letter.',
								prefixes: ['the', 'a']
							} );


	//===== ShowCode plugin for <pre> tag =====//

	$( '.showCode' ).sourcerer( 'js html css php' ); // Display all languages
	$( '.showCodeJS' ).sourcerer( 'js' ); // Display JS only
	$( '.showCodeHTML' ).sourcerer( 'html' ); // Display HTML only
	$( '.showCodePHP' ).sourcerer( 'php' ); // Display PHP only
	$( '.showCodeCSS' ).sourcerer( 'css' ); // Display CSS only


	//===== Calendar =====//

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();

	$( '#calendar' ).fullCalendar( {
									   header: {
										   left: 'prev,next',
										   center: 'title',
										   right: 'month,basicWeek,basicDay'
									   },
									   editable: true,
									   events: [
										   {
											   title: 'All day event',
											   start: new Date( y, m, 1 )
										   },
										   {
											   title: 'Long event',
											   start: new Date( y, m, 5 ),
											   end: new Date( y, m, 8 )
										   },
										   {
											   id: 999,
											   title: 'Repeating event',
											   start: new Date( y, m, 2, 16, 0 ),
											   end: new Date( y, m, 3, 18, 0 ),
											   allDay: false
										   },
										   {
											   id: 999,
											   title: 'Repeating event',
											   start: new Date( y, m, 9, 16, 0 ),
											   end: new Date( y, m, 10, 18, 0 ),
											   allDay: false
										   },
										   {
											   title: 'Actually any color could be applied for background',
											   start: new Date( y, m, 30, 10, 30 ),
											   end: new Date( y, m, d + 1, 14, 0 ),
											   allDay: false,
											   color: '#B55D5C'
										   },
										   {
											   title: 'Lunch',
											   start: new Date( y, m, 14, 12, 0 ),
											   end: new Date( y, m, 15, 14, 0 ),
											   allDay: false
										   },
										   {
											   title: 'Birthday PARTY',
											   start: new Date( y, m, 18 ),
											   end: new Date( y, m, 20 ),
											   allDay: false
										   },
										   {
											   title: 'Click for Google',
											   start: new Date( y, m, 27 ),
											   end: new Date( y, m, 29 ),
											   url: 'http://google.com/'
										   }
									   ]
								   } );


	//===== Dynamic data table =====//

	oTable = $( '#example' ).dataTable( {
											"bJQueryUI": true,
											"sPaginationType": "full_numbers",
											"sDom": '<""f>t<"F"lp>'
										} );


	//===== Form elements styling =====//

	$( "select, input:checkbox, input:radio, input:file" ).uniform();


	//===== Form validation engine =====//

	$( "#valid" ).validationEngine();


	//===== Datepickers =====//

	$( ".datepicker" ).datepicker( {
									   defaultDate: +7,
									   autoSize: true,
									   appendText: '(dd-mm-yyyy)',
									   dateFormat: 'dd-mm-yy'
								   } );

	$( ".datepickerInline" ).datepicker( {
											 defaultDate: +7,
											 autoSize: true,
											 appendText: '(dd-mm-yyyy)',
											 dateFormat: 'dd-mm-yy',
											 numberOfMonths: 1
										 } );


	//===== Progressbar (Jquery UI) =====//

	$( "#progressbar" ).progressbar( {
										 value: 37
									 } );


	//===== Tooltip =====//

	$( '.leftDir' ).tipsy( {fade: true, gravity: 'e'} );
	$( '.rightDir' ).tipsy( {fade: true, gravity: 'w'} );
	$( '.topDir' ).tipsy( {fade: true, gravity: 's'} );
	$( '.botDir' ).tipsy( {fade: true, gravity: 'n'} );


	//===== Information boxes =====//

	$( ".hideit" ).click( function () {
		$( this ).fadeTo( 200, 0.00, function () { //fade
			$( this ).slideUp( 300, function () { //slide up
				$( this ).remove(); //then remove from the DOM
			} );
		} );
	} );


	//=====Resizable table columns =====//

	var onSampleResized = function ( e ) {
		var columns = $( e.currentTarget ).find( "th" );
		var msg = "columns widths: ";
		columns.each( function () {
			msg += $( this ).width() + "px; ";
		} )
	};

	$( ".resize" ).colResizable( {
									 liveDrag: true,
									 gripInnerHtml: "<div class='grip'></div>",
									 draggingClass: "dragging",
									 onResize: onSampleResized} );


	//===== Left navigation submenu animation =====//

	$( "ul.sub li a" ).hover( function () {
		$( this ).stop().animate( { color: "#3a6fa5" }, 400 );
	}, function () {
		$( this ).stop().animate( { color: "#494949" }, 400 );
	} );


	//===== Image gallery control buttons =====//

	$( ".pics ul li" ).hover( function () {
		$( this ).children( ".actions" ).show( "fade", 200 );
	}, function () {
		$( this ).children( ".actions" ).hide( "fade", 200 );
	} );


	//===== Color picker =====//

	$( '#colorpickerField' ).ColorPicker( {
											  onSubmit: function ( hsb, hex, rgb, el ) {
												  $( el ).val( hex );
												  $( el ).ColorPickerHide();
											  },
											  onBeforeShow: function () {
												  $( this ).ColorPickerSetColor( this.value );
											  }
										  } ).bind( 'keyup', function () {
														$( this ).ColorPickerSetColor( this.value );
													} );


	//===== Autogrowing textarea =====//

	$( ".auto" ).autoGrow();


	//===== Autotabs. Inline data rows =====//

	$( '.onlyNums input' ).autotab_magic().autotab_filter( 'numeric' );
	$( '.onlyText input' ).autotab_magic().autotab_filter( 'text' );
	$( '.onlyAlpha input' ).autotab_magic().autotab_filter( 'alpha' );
	$( '.onlyRegex input' ).autotab_magic().autotab_filter( { format: 'custom', pattern: '[^0-9\.]' } );
	$( '.allUpper input' ).autotab_magic().autotab_filter( { format: 'alphanumeric', uppercase: true } );


	//===== jQuery UI sliders =====//

	$( ".uiSlider" ).slider();

	$( ".uiSliderInc" ).slider( {
									value: 100,
									min: 0,
									max: 500,
									step: 50,
									slide: function ( event, ui ) {
										$( "#amount" ).val( "$" + ui.value );
									}
								} );
	$( "#amount" ).val( "$" + $( ".uiSliderInc" ).slider( "value" ) );

	$( ".uiRangeSlider" ).slider( {
									  range: true,
									  min: 0,
									  max: 500,
									  values: [ 75, 300 ],
									  slide: function ( event, ui ) {
										  $( "#rangeAmount" ).val( "$" + ui.values[ 0 ] + " - $" + ui.values[ 1 ] );
									  }
								  } );
	$( "#rangeAmount" ).val( "$" + $( ".uiRangeSlider" ).slider( "values", 0 ) + " - $" + $( ".uiRangeSlider" ).slider( "values", 1 ) );

	$( ".uiMinRange" ).slider( {
								   range: "min",
								   value: 37,
								   min: 1,
								   max: 700,
								   slide: function ( event, ui ) {
									   $( "#minRangeAmount" ).val( "$" + ui.value );
								   }
							   } );
	$( "#minRangeAmount" ).val( "$" + $( ".uiMinRange" ).slider( "value" ) );

	$( ".uiMaxRange" ).slider( {
								   range: "max",
								   min: 1,
								   max: 100,
								   value: 20,
								   slide: function ( event, ui ) {
									   $( "#maxRangeAmount" ).val( ui.value );
								   }
							   } );
	$( "#maxRangeAmount" ).val( $( ".uiMaxRange" ).slider( "value" ) );


	$( "#eq > span" ).each( function () {
		// read initial values from markup and remove that
		var value = parseInt( $( this ).text(), 10 );
		$( this ).empty().slider( {
									  value: value,
									  range: "min",
									  animate: true,
									  orientation: "vertical"
								  } );
	} );


	//===== Breadcrumbs =====//

	$( "#breadCrumb" ).jBreadCrumb();


	//===== Autofocus =====//

	$( '.autoF' ).focus();


	//===== Tabs =====//

	$.fn.simpleTabs = function () {

		//Default Action
		$( this ).find( ".tab_content" ).hide(); //Hide all content
		$( this ).find( "ul.tabs li:first" ).addClass( "activeTab" ).show(); //Activate first tab
		$( this ).find( ".tab_content:first" ).show(); //Show first tab content

		//On Click Event
		$( "ul.tabs li" ).click( function () {
			$( this ).parent().parent().find( "ul.tabs li" ).removeClass( "activeTab" ); //Remove any "active" class
			$( this ).addClass( "activeTab" ); //Add "active" class to selected tab
			$( this ).parent().parent().find( ".tab_content" ).hide(); //Hide all tab content
			var activeTab = $( this ).find( "a" ).attr( "href" ); //Find the rel attribute value to identify the active tab + content
			$( activeTab ).show(); //Fade in the active content
			return false;
		} );

	};//end function

	$( "div[class^='widget']" ).simpleTabs(); //Run function on any div with class name of "Simple Tabs"


	//===== User nav dropdown =====//

	$( '.dd' ).click( function () {
		$( 'ul.menu_body' ).slideToggle( 200 );
	} );

	$( document ).bind( 'click', function ( e ) {
		var $clicked = $( e.target );
		if ( !$clicked.parents().hasClass( "dd" ) ) {
			$( "ul.menu_body" ).slideUp( 200 );
		}
	} );


	$( '.acts' ).click( function () {
		$( 'ul.actsBody' ).slideToggle( 100 );
	} );


	//===== Collapsible elements management =====//

	$( '.exp' ).collapsible( {
								 defaultOpen: 'current',
								 cookieName: 'navAct',
								 cssOpen: 'active corner',
								 cssClose: 'inactive',
								 speed: 300
							 } );

	$( '.opened' ).collapsible( {
									defaultOpen: 'opened,toggleOpened',
									cssOpen: 'inactive',
									cssClose: 'normal',
									speed: 200
								} );

	$( '.closed' ).collapsible( {
									defaultOpen: '',
									cssOpen: 'inactive',
									cssClose: 'normal',
									speed: 200
								} );


} );