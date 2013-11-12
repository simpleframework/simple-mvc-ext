
function __message_window_actions_init(actionFunc, messageComponent,
		ajaxComponent, frequency, closeDelay) {

	var _startMessage = function() {
		var w = Object.isString(messageComponent) ? $Actions[messageComponent]
				: messageComponent;
		if (w.isVisible()) {
			w.refreshContentRef();
		} else {
			(Object.isString(ajaxComponent) ? $Actions[ajaxComponent] : ajaxComponent)();
		}
		if (closeDelay > 0) {
			(function() { w.close(); }).delay(closeDelay);
		}
	};
	
	actionFunc.startMessage = function() {
		if (frequency == 0) {
			_startMessage();
		} else {
			actionFunc.pe = new PeriodicalExecuter(function() {
				_startMessage();
			}, frequency);
		}
	};
	
	actionFunc.stopMessage = function() {
		if (actionFunc.pe) {
			actionFunc.pe.stop();
			actionFunc.pe = null;
		}
	};
	
	actionFunc.ajaxRequestCallback = function(json) {
		if (!json['showMessageNotification']) return;
		var w = Object.isString(messageComponent) ? $Actions[messageComponent]
			: messageComponent;
		var t = document.viewport.getHeight() - w.options.height - 20;
		var l = document.viewport.getWidth() - w.options.width - 20;
		w.setOptions({ theme: 'popup', top: t, left : l });
		w();
	};
}