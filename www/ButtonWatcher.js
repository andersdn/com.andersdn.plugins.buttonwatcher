function ButtonWatcher() {
}

ButtonWatcher.prototype.watch: function(callback) {
	return cordova.exec(function(result) {
		ButtonWatcher.state = result.state;
		if(callback) {
			callback(result);
		}
	}, ButtonWatcher.fail, "ButtonWatcher", "watch", []);
};

ButtonWatcher.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.headsetdetection = new ButtonWatcher();
  return window.plugins.headsetdetection;
};

cordova.addConstructor(ButtonWatcher.install);