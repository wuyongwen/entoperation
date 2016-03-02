{
	root: {
		type: "com.chn.wx.listener.impl.process.SyncThreadMode",
		singleton: true,
		args: [{
			refer: "beanFactory", 
		}], 
		init: "init",
	}
}