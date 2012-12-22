/*
 * jQuery UI @VERSION
 *
 * Copyright (c) 2008 Paul Bakaus (ui.jquery.com)
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI
 */
(function($) {

/** jQuery core modifications and additions **/

var _remove = $.fn.remove;
$.fn.remove = function() {
	// Safari has a native remove event which actually removes DOM elements,
	// so we have to use triggerHandler instead of trigger (#3037).
	$("*", this).add(this).each(function() {
		$(this).triggerHandler("remove");
	});
	return _remove.apply(this, arguments );
};

function isVisible(element) {
	function checkStyles(element) {
		var style = element.style;
		return (style.display != 'none' && style.visibility != 'hidden');
	}
	
	var visible = checkStyles(element);
	
	(visible && $.each($.dir(element, 'parentNode'), function() {
		return (visible = checkStyles(this));
	}));
	
	return visible;
}

$.extend($.expr[':'], {
	data: function(a, i, m) {
		return $.data(a, m[3]);
	},
	
	// TODO: add support for object, area
	tabbable: function(a, i, m) {
		var nodeName = a.nodeName.toLowerCase();
		
		return (
			// in tab order
			a.tabIndex >= 0 &&
			
			( // filter node types that participate in the tab order
				
				// anchor tag
				('a' == nodeName && a.href) ||
				
				// enabled form element
				(/input|select|textarea|button/.test(nodeName) &&
					'hidden' != a.type && !a.disabled)
			) &&
			
			// visible on page
			isVisible(a)
		);
	}
});

$.keyCode = {
	BACKSPACE: 8,
	CAPS_LOCK: 20,
	COMMA: 188,
	CONTROL: 17,
	DELETE: 46,
	DOWN: 40,
	END: 35,
	ENTER: 13,
	ESCAPE: 27,
	HOME: 36,
	INSERT: 45,
	LEFT: 37,
	NUMPAD_ADD: 107,
	NUMPAD_DECIMAL: 110,
	NUMPAD_DIVIDE: 111,
	NUMPAD_ENTER: 108,
	NUMPAD_MULTIPLY: 106,
	NUMPAD_SUBTRACT: 109,
	PAGE_DOWN: 34,
	PAGE_UP: 33,
	PERIOD: 190,
	RIGHT: 39,
	SHIFT: 16,
	SPACE: 32,
	TAB: 9,
	UP: 38
};

// WAI-ARIA Semantics
var isFF2 = $.browser.mozilla && (parseFloat($.browser.version) < 1.9);
$.fn.extend({
	ariaRole: function(role) {
		return (role !== undefined
			
			// setter
			? this.attr("role", isFF2 ? "wairole:" + role : role)
			
			// getter
			: (this.attr("role") || "").replace(/^wairole:/, ""));
	},
	
	ariaState: function(state, value) {
		return (value !== undefined
			
			// setter
			? this.each(function(i, el) {
				(isFF2
					? el.setAttributeNS("http://www.w3.org/2005/07/aaa",
						"aaa:" + state, value)
					: $(el).attr("aria-" + state, value));
			})
			
			// getter
			: this.attr(isFF2 ? "aaa:" + state : "aria-" + state));
	}
});

// $.widget is a factory to create jQuery plugins
// taking some boilerplate code out of the plugin code
// created by Scott González and Jörn Zaefferer
function getter(namespace, plugin, method, args) {
	function getMethods(type) {
		var methods = $[namespace][plugin][type] || [];
		return (typeof methods == 'string' ? methods.split(/,?\s+/) : methods);
	}
	
	var methods = getMethods('getter');
	if (args.length == 1 && typeof args[0] == 'string') {
		methods = methods.concat(getMethods('getterSetter'));
	}
	return ($.inArray(method, methods) != -1);
}

$.widget = function(name, prototype) {
	var namespace = name.split(".")[0];
	name = name.split(".")[1];
	
	// create plugin method
	$.fn[name] = function(options) {
		var isMethodCall = (typeof options == 'string'),
			args = Array.prototype.slice.call(arguments, 1);
		
		// prevent calls to internal methods
		if (isMethodCall && options.substring(0, 1) == '_') {
			return this;
		}
		
		// handle getter methods
		if (isMethodCall && getter(namespace, name, options, args)) {
			var instance = $.data(this[0], name);
			return (instance ? instance[options].apply(instance, args)
				: undefined);
		}
		
		// handle initialization and non-getter methods
		return this.each(function() {
			var instance = $.data(this, name);
			
			// constructor
			(!instance && !isMethodCall &&
				$.data(this, name, new $[namespace][name](this, options)));
			
			// method call
			(instance && isMethodCall && $.isFunction(instance[options]) &&
				instance[options].apply(instance, args));
		});
	};
	
	// create widget constructor
	$[namespace] = $[namespace] || {};
	$[namespace][name] = function(element, options) {
		var self = this;
		
		this.widgetName = name;
		this.widgetEventPrefix = $[namespace][name].eventPrefix || name;
		this.widgetBaseClass = namespace + '-' + name;
		
		this.options = $.extend({},
			$.widget.defaults,
			$[namespace][name].defaults,
			$.metadata && $.metadata.get(element)[name],
			options);
		
		this.element = $(element)
			.bind('setData.' + name, function(e, key, value) {
				return self._setData(key, value);
			})
			.bind('getData.' + name, function(e, key) {
				return self._getData(key);
			})
			.bind('remove', function() {
				return self.destroy();
			});
		
		this._init();
	};
	
	// add widget prototype
	$[namespace][name].prototype = $.extend({}, $.widget.prototype, prototype);
	
	// TODO: merge getter and getterSetter properties from widget prototype
	// and plugin prototype
	$[namespace][name].getterSetter = 'option';
};

$.widget.prototype = {
	_init: function() {},
	destroy: function() {
		this.element.removeData(this.widgetName);
	},
	
	option: function(key, value) {
		var options = key,
			self = this;
		
		if (typeof key == "string") {
			if (value === undefined) {
				return this._getData(key);
			}
			options = {};
			options[key] = value;
		}
		
		$.each(options, function(key, value) {
			self._setData(key, value);
		});
	},
	_getData: function(key) {
		return this.options[key];
	},
	_setData: function(key, value) {
		this.options[key] = value;
		
		if (key == 'disabled') {
			this.element[value ? 'addClass' : 'removeClass'](
				this.widgetBaseClass + '-disabled');
		}
	},
	
	enable: function() {
		this._setData('disabled', false);
	},
	disable: function() {
		this._setData('disabled', true);
	},
	
	_trigger: function(type, e, data) {
		var eventName = (type == this.widgetEventPrefix
			? type : this.widgetEventPrefix + type);
		e = e  || $.event.fix({ type: eventName, target: this.element[0] });
		return this.element.triggerHandler(eventName, [e, data], this.options[type]);
	}
};

$.widget.defaults = {
	disabled: false
};


/** jQuery UI core **/

$.ui = {
	// $.ui.plugin is deprecated.  Use the proxy pattern instead.
	plugin: {
		add: function(module, option, set) {
			var proto = $.ui[module].prototype;
			for(var i in set) {
				proto.plugins[i] = proto.plugins[i] || [];
				proto.plugins[i].push([option, set[i]]);
			}
		},
		call: function(instance, name, args) {
			var set = instance.plugins[name];
			if(!set) { return; }
			
			for (var i = 0; i < set.length; i++) {
				if (instance.options[set[i][0]]) {
					set[i][1].apply(instance.element, args);
				}
			}
		}	
	},
	cssCache: {},
	css: function(name) {
		if ($.ui.cssCache[name]) { return $.ui.cssCache[name]; }
		var tmp = $('<div class="ui-gen">').addClass(name).css({position:'absolute', top:'-5000px', left:'-5000px', display:'block'}).appendTo('body');
		
		//if (!$.browser.safari)
			//tmp.appendTo('body');
		
		//Opera and Safari set width and height to 0px instead of auto
		//Safari returns rgba(0,0,0,0) when bgcolor is not set
		$.ui.cssCache[name] = !!(
			(!(/auto|default/).test(tmp.css('cursor')) || (/^[1-9]/).test(tmp.css('height')) || (/^[1-9]/).test(tmp.css('width')) || 
			!(/none/).test(tmp.css('backgroundImage')) || !(/transparent|rgba\(0, 0, 0, 0\)/).test(tmp.css('backgroundColor')))
		);
		try { $('body').get(0).removeChild(tmp.get(0));	} catch(e){}
		return $.ui.cssCache[name];
	},
	disableSelection: function(el) {
		return $(el)
			.attr('unselectable', 'on')
			.css('MozUserSelect', 'none')
			.bind('selectstart.ui', function() { return false; });
	},
	enableSelection: function(el) {
		return $(el)
			.attr('unselectable', 'off')
			.css('MozUserSelect', '')
			.unbind('selectstart.ui');
	},
	hasScroll: function(e, a) {
		
		//If overflow is hidden, the element might have extra content, but the user wants to hide it
		if ($(e).css('overflow') == 'hidden') { return false; }
		
		var scroll = (a && a == 'left') ? 'scrollLeft' : 'scrollTop',
			has = false;
		
		if (e[scroll] > 0) { return true; }
		
		// TODO: determine which cases actually cause this to happen
		// if the element doesn't have the scroll set, see if it's possible to
		// set the scroll
		e[scroll] = 1;
		has = (e[scroll] > 0);
		e[scroll] = 0;
		return has;
	}
};


/** Mouse Interaction Plugin **/

$.ui.mouse = {
	_mouseInit: function() {
		var self = this;
	
		this.element.bind('mousedown.'+this.widgetName, function(e) {
			return self._mouseDown(e);
		});
		
		// Prevent text selection in IE
		if ($.browser.msie) {
			this._mouseUnselectable = this.element.attr('unselectable');
			this.element.attr('unselectable', 'on');
		}
		
		this.started = false;
	},
	
	// TODO: make sure destroying one instance of mouse doesn't mess with
	// other instances of mouse
	_mouseDestroy: function() {
		this.element.unbind('.'+this.widgetName);
		
		// Restore text selection in IE
		($.browser.msie
			&& this.element.attr('unselectable', this._mouseUnselectable));
	},
	
	_mouseDown: function(e) {
		// we may have missed mouseup (out of window)
		(this._mouseStarted && this._mouseUp(e));
		
		this._mouseDownEvent = e;
		
		var self = this,
			btnIsLeft = (e.which == 1),
			elIsCancel = (typeof this.options.cancel == "string" ? $(e.target).parents().add(e.target).filter(this.options.cancel).length : false);
		if (!btnIsLeft || elIsCancel || !this._mouseCapture(e)) {
			return true;
		}
		
		this.mouseDelayMet = !this.options.delay;
		if (!this.mouseDelayMet) {
			this._mouseDelayTimer = setTimeout(function() {
				self.mouseDelayMet = true;
			}, this.options.delay);
		}
		
		if (this._mouseDistanceMet(e) && this._mouseDelayMet(e)) {
			this._mouseStarted = (this._mouseStart(e) !== false);
			if (!this._mouseStarted) {
				e.preventDefault();
				return true;
			}
		}
		
		// these delegates are required to keep context
		this._mouseMoveDelegate = function(e) {
			return self._mouseMove(e);
		};
		this._mouseUpDelegate = function(e) {
			return self._mouseUp(e);
		};
		$(document)
			.bind('mousemove.'+this.widgetName, this._mouseMoveDelegate)
			.bind('mouseup.'+this.widgetName, this._mouseUpDelegate);
		
		return false;
	},
	
	_mouseMove: function(e) {
		// IE mouseup check - mouseup happened when mouse was out of window
		if ($.browser.msie && !e.button) {
			return this._mouseUp(e);
		}
		
		if (this._mouseStarted) {
			this._mouseDrag(e);
			return false;
		}
		
		if (this._mouseDistanceMet(e) && this._mouseDelayMet(e)) {
			this._mouseStarted =
				(this._mouseStart(this._mouseDownEvent, e) !== false);
			(this._mouseStarted ? this._mouseDrag(e) : this._mouseUp(e));
		}
		
		return !this._mouseStarted;
	},
	
	_mouseUp: function(e) {
		$(document)
			.unbind('mousemove.'+this.widgetName, this._mouseMoveDelegate)
			.unbind('mouseup.'+this.widgetName, this._mouseUpDelegate);
		
		if (this._mouseStarted) {
			this._mouseStarted = false;
			this._mouseStop(e);
		}
		
		return false;
	},
	
	_mouseDistanceMet: function(e) {
		return (Math.max(
				Math.abs(this._mouseDownEvent.pageX - e.pageX),
				Math.abs(this._mouseDownEvent.pageY - e.pageY)
			) >= this.options.distance
		);
	},
	
	_mouseDelayMet: function(e) {
		return this.mouseDelayMet;
	},
	
	// These are placeholder methods, to be overriden by extending plugin
	_mouseStart: function(e) {},
	_mouseDrag: function(e) {},
	_mouseStop: function(e) {},
	_mouseCapture: function(e) { return true; }
};

$.ui.mouse.defaults = {
	cancel: null,
	distance: 1,
	delay: 0
};

})(jQuery);






/*
 * jQuery UI Spinner @VERSION
 *
 * Copyright (c) 2008 jQuery
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://docs.jquery.com/UI/Spinner
 *
 * Depends:
 *  ui.core.js
 */
(function($) {

$.widget('ui.spinner', {
	_init: function() {
		this._trigger('init', null, this.ui(null));
		
		// perform data bind on generic objects
		if (typeof this.options.items[0] == 'object' && !this.element.is('input')) {
			var data = this.options.items;
			for (var i=0; i<data.length; i++) {
				this._addItem(data[i]);
			}
		}
		
		// check for decimals in steppinng and set _decimals as internal
		this._decimals = parseInt(this.options.decimals, 10);
		if (this.options.stepping.toString().indexOf('.') != -1) {
			var s = this.options.stepping.toString();
			this._decimals = s.slice(s.indexOf('.')+1, s.length).length;
		}
		
		//Initialize needed constants
		var self = this;
		this.element
			.addClass('ui-spinner-box')
			.attr('autocomplete', 'off'); // switch off autocomplete in opera
		
		this._setValue( isNaN(this._getValue()) ? this.options.start : this._getValue() );
		
		this.element
		.wrap('<div>')
		.parent()
			.addClass('ui-spinner')
			.append('<button class="ui-spinner-up" type="button"></button>')
			.find('.ui-spinner-up')
				.bind('mousedown', function(e) {
					$(this).addClass('ui-spinner-pressed');
					if (!self.counter) {
						self.counter = 1;
					}
					self._mousedown(100, '_up', e);
				})
				.bind('mouseup', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					if (self.counter == 1) {
						self._up(e);
					}
					self._mouseup(e);
				})
				.bind('mouseout', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					if (self.timer) {
						self._mouseup(e);
					}
				})
				// mousedown/mouseup capture first click, now handle second click
				.bind('dblclick', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					self._up(e);
					self._mouseup(e);
				})
				.bind('keydown.spinner', function(e) {
					var KEYS = $.keyCode;
					if (e.keyCode == KEYS.SPACE || e.keyCode == KEYS.ENTER) {
						$(this).addClass('ui-spinner-pressed');
						if (!self.counter) {
							self.counter = 1;
						}
						self._up.call(self, e);
					} else if (e.keyCode == KEYS.DOWN || e.keyCode == KEYS.RIGHT) {
						self.element.siblings('.ui-spinner-down').focus();
					} else if (e.keyCode == KEYS.LEFT) {
						self.element.focus();
					}
				})
				.bind('keyup.spinner', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					self.counter = 0;
					self._propagate('change', e);
				})
			.end()
			.append('<button class="ui-spinner-down" type="button"></button>')
			.find('.ui-spinner-down')
				.bind('mousedown', function(e) {
					$(this).addClass('ui-spinner-pressed');
					if (!self.counter) {
						self.counter = 1;
					}
					self._mousedown(100, '_down', e);
				})
				.bind('mouseup', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					if (self.counter == 1) {
						self._down();
					}
					self._mouseup(e);
				})
				.bind('mouseout', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					if (self.timer) {
						self._mouseup(e);
					}
				})
				// mousedown/mouseup capture first click, now handle second click
				.bind('dblclick', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					self._down(e);
					self._mouseup(e);
				})
				.bind('keydown.spinner', function(e) {
					var KEYS = $.keyCode;
					if (e.keyCode == KEYS.SPACE || e.keyCode == KEYS.ENTER) {
						$(this).addClass('ui-spinner-pressed');
						if (!self.counter) {
							self.counter = 1;
						}
						self._down.call(self, e);
					} else if (e.keyCode == KEYS.UP || e.keyCode == KEYS.LEFT) {
						self.element.siblings('.ui-spinner-up').focus();
					}
				})
				.bind('keyup.spinner', function(e) {
					$(this).removeClass('ui-spinner-pressed');
					self.counter = 0;
					self._propagate('change', e);
				})
			.end();
		
		// DataList: Set contraints for object length and step size. 
		// Manipulate height of spinner.
		this._items = this.element.children().length;
		if (this._items > 1) {
			var height = this.element.outerHeight()/this._items;
			this.element
			.addClass('ui-spinner-list')
			.height(height)
			.children()
				.addClass('ui-spinner-listitem')
				.height(height)
				.css('overflow', 'hidden')
			.end()
			.parent()
				.height(height)
			.end();
			this.options.stepping = 1;
			this.options.min = 0;
			this.options.max = this._items-1;
		}
		
		this.element
		.bind('keydown.spinner', function(e) {
			if (!self.counter) {
				self.counter = 1;
			}
			return self._keydown.call(self, e);
		})
		.bind('keyup.spinner', function(e) {
			self.counter = 0;
			self._propagate('change', e);
		})
		.bind('blur.spinner', function(e) {
			self._cleanUp();
		});
		
		if ($.fn.mousewheel) {
			this.element.mousewheel(function(e, delta) {
				self._mousewheel(e, delta);
			});
		}
	},
	
	_constrain: function() {
		if (this.options.min != undefined && this._getValue() < this.options.min) {
			this._setValue(this.options.min);
		}
		if (this.options.max != undefined && this._getValue() > this.options.max) {
			this._setValue(this.options.max);
		}
	},
	_cleanUp: function() {
		this._setValue(this._getValue());
		this._constrain();
	},
	_spin: function(d, e) {
		if (this.disabled) {
			return;
		}
		
		if (isNaN(this._getValue())) {
			this._setValue(this.options.start);
		}
		this._setValue(this._getValue() + (d == 'up' ? 1:-1) * (this.options.incremental && this.counter > 100 ? (this.counter > 200 ? 100 : 10) : 1) * this.options.stepping);
		this._animate(d);
		this._constrain();
		if (this.counter) {
			this.counter++;
		}
		this._propagate('spin', e);
	},
	_down: function(e) {
		this._spin('down', e);
		this._propagate('down', e);
	},
	_up: function(e) {
		this._spin('up', e);
		this._propagate('up', e);
	},
	_mousedown: function(i, d, e) {
		var self = this;
		i = i || 100;
		if (this.timer) {
			window.clearInterval(this.timer);
			this.timer = 0;
		}
		this.timer = window.setInterval(function() {
			self[d](e);
			if (self.counter > 20) {
				self._mousedown(20, d, e);
			}
		}, i);
	},
	_mouseup: function(e) {
		this.counter = 0;
		if (this.timer) {
			window.clearInterval(this.timer);
			this.timer = 0;
		}
		this.element[0].focus();
		this._propagate('change', e);
	},
	_keydown: function(e) {
		var KEYS = $.keyCode;
		
		if (e.keyCode == KEYS.UP) {
			this._up(e);
		}
		if (e.keyCode == KEYS.DOWN) {
			this._down(e);
		}
		if (e.keyCode == KEYS.HOME) {
			//Home key goes to min, if defined, else to start
			this._setValue(this.options.min || this.options.start);
		}
		if (e.keyCode == KEYS.END && this.options.max != undefined) {
			//End key goes to maximum
			this._setValue(this.options.max);
		}
		return (e.keyCode == KEYS.TAB || e.keyCode == KEYS.BACKSPACE ||
			e.keyCode == KEYS.LEFT || e.keyCode == KEYS.RIGHT || e.keyCode == KEYS.PERIOD || 
			e.keyCode == KEYS.NUMPAD_DECIMAL || e.keyCode == KEYS.NUMPAD_SUBTRACT || 
			(e.keyCode >= 96 && e.keyCode <= 105) || // add support for numeric keypad 0-9
			(/[0-9\-\.]/).test(String.fromCharCode(e.keyCode))) ? true : false;
	},
	_mousewheel: function(e, delta) {
		var self = this;
		delta = ($.browser.opera ? -delta / Math.abs(delta) : delta);
		(delta > 0 ? self._up(e) : self._down(e));
		if (self.timeout) {
			window.clearTimeout(self.timeout);
			self.timeout = 0;
		}
		self.timeout = window.setTimeout(function(){self._propagate('change', e)}, 400);
		e.preventDefault();
	},
	_getValue: function() {
		return parseFloat(this.element.val().replace(/[^0-9\-\.]/g, ''));
	},
	_setValue: function(newVal) {
		if (isNaN(newVal)) {
			newVal = this.options.start;
		}
		this.element.val(
			this.options.currency ? 
				$.ui.spinner.format.currency(newVal, this.options.currency) : 
				$.ui.spinner.format.number(newVal, this._decimals)
		);
	},
	_animate: function(d) {
		if (this.element.hasClass('ui-spinner-list') && ((d == 'up' && this._getValue() <= this.options.max) || (d == 'down' && this._getValue() >= this.options.min)) ) {
			this.element.animate({marginTop: '-' + this._getValue() * this.element.parent().height() }, {
				duration: 'fast',
				queue: false
			});
		}
	},
	_addItem: function(obj, fmt) {
		if (!this.element.is('input')) {
			var wrapper = 'div';
			if (this.element.is('ol') || this.element.is('ul')) {
				wrapper = 'li';
			}
			var html = obj; // string or object set it to html first
			
			if (typeof obj == 'object') {
				var format = (fmt !== undefined ? fmt : this.options.format);
				
				html = format.replace(/%(\(([^)]+)\))?/g, 
					(function(data){
						return function(match, a, lbl) { 
							if (!lbl) {
								for (var itm in data) {
									return data[itm]; // return the first item only
								}
							} else {
								return data[lbl];
							}
						};
					})(obj)
				);
			}
			this.element.append('<'+ wrapper +' class="ui-spinner-data">'+ html + '</'+ wrapper +'>');
		}
	},
	
	plugins: {},
	ui: function(e) {
		return {
			options: this.options,
			element: this.element,
			value: this._getValue(),
			add: this._addItem
		};
	},
	_propagate: function(n,e) {
		$.ui.plugin.call(this, n, [e, this.ui()]);
		return this.element.triggerHandler(n == 'spin' ? n : 'spin'+n, [e, this.ui()], this.options[n]);
	},
	destroy: function() {
		if (!$.data(this.element[0], 'spinner')) {
			return;
		}
		if ($.fn.mousewheel) {
			this.element.unmousewheel();
		}
		this.element
			.removeClass('ui-spinner-box ui-spinner-list')
			.removeAttr('disabled')
			.removeAttr('autocomplete')
			.removeData('spinner')
			.unbind('.spinner')
			.siblings()
				.remove()
			.end()
			.children()
				.removeClass('ui-spinner-listitem')
				.remove('.ui-spinner-data')
			.end()
			.parent()
				.removeClass('ui-spinner ui-spinner-disabled')
				.before(this.element.clone())
				.remove()
			.end();
	},
	enable: function() {
		this.element
			.removeAttr('disabled')
			.siblings()
				.removeAttr('disabled')
			.parent()
				.removeClass('ui-spinner-disabled');
		this.disabled = false;
	},
	disable: function() {
		this.element
			.attr('disabled', true)
			.siblings()
				.attr('disabled', true)
			.parent()
				.addClass('ui-spinner-disabled');
		this.disabled = true;
	}
});

$.extend($.ui.spinner, {
	defaults: {
		decimals: 0,
		stepping: 1,
		start: 0,
		incremental: true,
		currency: false,
		format: '%',
		items: []
	},
	format: {
		currency: function(num, sym) {
			num = isNaN(num) ? 0 : num;
			return (num !== Math.abs(num) ? '-' : '') + sym + this.number(Math.abs(num), 2);
		},
		number: function(num, dec) {
			var regex = /(\d+)(\d{3})/;
			for (num = isNaN(num) ? 0 : parseFloat(num,10).toFixed(dec); regex.test(num); num=num.replace(regex, '$1,$2'));
			return num;
		}
	}
});

})(jQuery);
