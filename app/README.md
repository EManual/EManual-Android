Notes
===
 

v1.2.0 2014-08-25
===

### 豌豆荚SDK 
豌豆荚的sdk与EManual使用的PagerSlidingTabStrip库产生了冲突，解决如下：
* 删除psts_attrs.xml 如下属性
```xml
		<attr name="pstsIndicatorColor" format="color" />
        <attr name="pstsUnderlineColor" format="color" />
        <attr name="pstsDividerColor" format="color" />
        <attr name="pstsIndicatorHeight" format="dimension" />
        <attr name="pstsUnderlineHeight" format="dimension" />
        <attr name="pstsDividerPadding" format="dimension" />
        <attr name="pstsTabPaddingLeftRight" format="dimension" />
        <attr name="pstsScrollOffset" format="dimension" />
        <attr name="pstsTabBackground" format="reference" />
        <attr name="pstsShouldExpand" format="boolean" />
        <attr name="pstsTextAllCaps" format="boolean" />
```

* 以library的形式加入豌豆荚sdk，减少sdk带来的侵入性
* 删除豌豆荚的AndroidManifest.xml 中的acitivity,service,receiver

### 友盟统计
在TopicList.java 模块中统计跳转到模块标题的页面信息
```java
	public void onResume() {
	    super.onResume();
	    if(title!= null && !title.equals("")){
	    	MobclickAgent.onPageStart(title);  
	    }else{
	    	MobclickAgent.onPageStart("TopicList");  
	    }
	    
	}
	public void onPause() {
	    super.onPause();
	    if(title!= null && !title.equals("")){
	    	MobclickAgent.onPageEnd(title);  
	    }else{
	    	MobclickAgent.onPageEnd("TopicList");  
	    }
	}
```