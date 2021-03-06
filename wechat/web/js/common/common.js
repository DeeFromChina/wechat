$(".dropdown .form-control").click(function(){
	if(this.parentNode.getElementsByTagName("button") == undefined){
		return;
	}
	this.parentNode.getElementsByTagName("button")[0].click();
});

function getid(id){
	return document.getElementById(id);
}
function saveValue(key,value){
	window.top.map[key] = value;
}
function loadValue(key){
	return window.top.map[key];
}
function removeValue(key){
	delete window.top.map[key];
}
/**
 * 填充下拉框
 * listId:下拉框id
 * listValue:下拉框数据
 * index:多层
 * method:方法（this作为参数）
 * */
function setList(options){
	var listId = options.listId;
	var placeholder = options.placeholder;
	var listValue = options.listValue;
	var index = options.index;
	var method = options.method;
	var widthClass = options.widthClass;
	var selectMeum = "selectMeum";
	var inputClass = options.inputClass;
	
	if(widthClass == undefined){
		widthClass = "wi100";
	}else{
		selectMeum = " selectMeum2 " + widthClass;
	}
	
	if(inputClass == undefined){
		inputClass = "input-class";
	}else{
		inputClass = " h41 " + inputClass;
	}
	
	if(placeholder == undefined){
		placeholder = "";
	}
	
	if(method == undefined){
		method = "";
	}
	
	if(index == undefined){
		index = 0;
	}
	
	var level = index;
	var inn = "";
	inn += "<div class=\"dropdown " + inputClass + "\">";
	inn += "<div class=\""+widthClass+"\">";
	inn += "<input type=\"text\" id=\""+listId+"Value\" name=\""+listId+"Value\" class=\"form-control\" readOnly=\"readOnly\" placeholder=\""+placeholder+"\" />";
	inn += "<input type=\"hidden\" name=\""+listId+"\" />";
	inn += "</div>";
	inn += "<button type=\"button\" class=\"btn dropdown-toggle selectBtn\" data-toggle=\"dropdown\">";
	inn += "<span class=\"caret\"></span>";
	inn += "</button>";
	if(listValue != '' && listValue != undefined){
		inn += "<ul class=\"dropdown-menu " + selectMeum + "\" role=\"menu\" aria-labelledby=\"dropdownMenu\">";
		inn += addListChild(listId,listValue,index,method,selectMeum);
		inn += "</ul>";
	}else{
		inn += "<ul class=\"dropdown-menu " + selectMeum + "\" role=\"menu\" aria-labelledby=\"dropdownMenu\">";
		inn+="<li role='presentation'>"
				+"<a class='selectValue' role='menuitem' >"
					+placeholder
				+"</a>";
		inn+="</li>";
		inn += "</ul>";
	}
	inn += "</div>";
	getid(listId).innerHTML = inn;
}
function addListChild(listId,listValue,index,method,selectMeum){
	var level = index;
	var inn = "";
	var cls = "";
	if(level > 1){
		cls = "class='dropdown-submenu'";
	}
	for(var i in listValue){
		inn+="<li role='presentation' "+cls+">"
				+"<a class='selectValue' role='menuitem' id=\'"+listValue[i].id+"\' onclick=\'selectValue(this,\""+listId+"\");"+method+"\'>"
					+listValue[i].value
				+"</a>";
		if(level > 1){
			inn+="<ul class='dropdown-menu " + selectMeum + "'>";
			inn+=setList(listId,listValue,level-1,method);
			inn+="</ul>";
		}
		inn+="</li>";
	}
	return inn;
}
/**
 * 选择下拉框
 * inputId:下拉显示框id
 * selectId:选择数据id
 * */
function selectValue(obj,targetId){
	$("input[name='"+targetId+"']").val(obj.id);
	$("#"+targetId+"Value").val(obj.innerText);
}
/*向ObjId元素赋值*/
function setElementValue(objId,val,type){
	if(val == undefined){
		return;
	}
	if(type == 'select'){
		$("input[name='"+objId+"']").val(val);
		var context = $("#"+objId).find("a[id='"+val+"']").text();
		$("#"+objId+"Value").val(context);
	}
	if(type == 'checkbox'){
		$('#'+objId).find("input[id='"+objId+"_"+val+"']").parent().addClass("checked");
	}
	if(type == 'circle'){
		$('#'+objId+val).removeClass("circleChkB");
		$('#'+objId+val).addClass("circleChkA");
	}
}
/**
 * 在业务字典找到对应单词
 * key:一级类型
 * val:里面的key
 * */
function getWord(key,val){
	var obj = jQuery.parseJSON(key);
	for(var i in obj){
		if(obj[i].id == val){
			return obj[i].value;
		}
	}
	return "";
}
/**
 * url:跳转地址
 * isPass:是否能跳转;
 * */
function jumpPage(url,isPass,message){
	if(isServer){
		top.location.href=url;
	}else{
		alert(message);
	}
}
/**
 * urlnum:跳转地址
 * */
function to(urlnum){
	var page = pages[urlnum];
	var timestamp = Date.parse(new Date()); 
	page = jspPath + page + "?page=" + timestamp;
	top.location.href=page;
}
function go(urlnum){
	var page = pages[urlnum];
	page = jspPath + page;
	$('#mainFrame').attr("src",page);
}
/**
 * 打开新窗口
 * url:页面路径（http://localhost:8080/hibernateMvc/autojsp/+url）
 * title:窗口标题
 * width:窗口宽度
 * height:窗口高度
 * dom:目标页面document
 * */
function openWindow(pageId,url,title,width,height){
	var targetDocument = document;
	doc = window.top.document;
	
	if(url != ''){
		url = jspPath + url;
	}
	var pageGroup = doc.getElementById("pageGroup");
	var divs = $(pageGroup).find("div");
	var marginTop = (window.screen.height-height-80)/3;//80是浏览器头部高度
	
	var titleHeiight = $(doc.getElementById("headTitle")).height();
	if(doc.getElementById("headTitle").style.display != "none"){
		if(marginTop < titleHeiight){
			marginTop = titleHeiight;
		}
	}
	
	var timestamp = Date.parse(new Date());
	timestamp = timestamp / 1000;
	if(targetDocument != undefined){
		saveValue(pageId,targetDocument);
	}
	var pageStr = "<div class='overflowY modal fade' id='myPage"+timestamp+"' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>"
						+"<div class='modal-dialog' style='width:"+width+";height:"+height+";margin-top:"+marginTop+";'>"
							+"<div class='modal-content'>"
								+"<div class='modal-header'>"
									+"<a id='"+pageId+"' class='close' data-dismiss='modal' aria-hidden='true' onclick='closeWindow(\"myPage"+timestamp+"\",\""+pageId+"\")'>"
										+"&times;"
									+"</a>"
									+"<h4 class='modal-title' id='myModalLabel'>"
										+title
									+"</h4>"
								+"</div>"
								+"<div class='modal-body' style='overflow:auto;'>"
									+"<iframe src='"+url+"' class='wh100' scrolling='yes' frameborder='0'></iframe>"
								+"</div>"
							+"</div>"
						+"</div>"
					+"</div>";
	$(pageGroup).append(pageStr);
	var obj = doc.getElementById("myPage"+timestamp);
	if(divs.length > 0){
		$(obj).modal({backdrop: 'static', keyboard: false, backdrop: null, cover: "myPage"+timestamp});
		$(obj).css("padding-left","0");
	}else{
		$(obj).modal({backdrop: 'static', keyboard: false, cover: "myPage"+timestamp});
	}
	doc.body.style.overflowY = "hidden";
	if($(doc.body).height() - titleHeiight < height){
		$(obj).css("overflow-y","auto");
	}
	$(obj).draggable();
}
/**
 * 关闭遮罩层
 * objId:打开窗口id
 * */
function closeWindow(objId,pageId){
	if(loadValue(pageId) != undefined){
		var targetDocument = loadValue(pageId);
		targetDocument.getElementById("closeListenerBtn").click();
		removeValue(pageId);
	}
	$("#"+objId).modal('hide');
	$("#"+objId).remove();
	var pageGroup = window.top.document.getElementById("pageGroup");
	var divs = $(pageGroup).find("div[id^='myPage']");
	if(divs.length == 0){
		window.top.document.body.style.overflowY = "auto";
	}
	$(window.top.document.getElementById(objId)).remove();
}
function closedialog(){
	$(document.body).css("overflow-y","auto");
	closeListener();
}
function closeWin(pageId){
	$(parent.document.getElementById(pageId)).click();
}
/**
 * 计算frame高度
 * */
function countFrameHeight(){
	var pdoc = parent.document;
	pdoc.getElementById("mainFrame").height = 0;
	pdoc.getElementById("mainFrame").height = document.body.height;
	//pdoc.getElementById("mainFrame").style.marginTop = parent.$("#headTitle").height() - 10;//不知道为什么会多出10高度
}
/**
 * dataForm垂直居中
 * 
 * */
function dataFormVcenter(ispad){
	var windowHeight = window.screen.availHeight;//屏幕可用工作区高度
	
	var formHeight = $('#dataForm').height();
	var bodyHeight = $(parent.document.body).height();
	var headHeight = 0;
	if(parent.$("#headTitle") != undefined){
		headHeight = $("#headTitle",parent.document).height() - 10;
	}
	var bh = 0;
	if(parent.$("#bottomDiv") != undefined){
		bh = parent.$("#bottomDiv").height();
	}
	
	var flag = true;
	if(windowHeight < bh + formHeight + headHeight){
		bodyHeight = bh + formHeight + headHeight;
		flag = false;
	}else{
		bodyHeight = windowHeight;
	}
	var marginTH = 0;
	if(ispad != undefined){
		flag = false;
	}
	if(flag){
		document.body.height = bodyHeight - bh - 68;
		marginTH = (document.body.height - formHeight)/2;
		$('#dataForm').css("padding-top",marginTH);
	}else{
		document.body.height = bodyHeight;//给足够的边距
		$('#dataForm').css("padding-top",headHeight+50);//dataForm远大于屏幕
		$('#dataForm').css("padding-bottom",bh);
	}
}
function setSrc(id,path,isbase){
	if(isbase != undefined){
		path = rootPath + path;
	}else{
		path = rootPath+"img/" + path;
	}
	$('#'+id).attr('src',path);
}
/**
 * 判断长宽返回判定
 * targetId:要判断的容器id
 * flag:判断类型（width,height）
 * size:判断大小
 * trueS:大于判断大小时的返回值
 * falseS:小于判断大小时的返回值
 * */
function judgeSize(targetId,flag,size,trueS,falseS){
	if(flag == 'width'){
		if($('#'+targetId).width() < size){
			return falseS;
		}else{
			return trueS;
		}
	}else{
		if($('#'+targetId).height() < size){
			return falseS;
		}else{
			return trueS;
		}
	}
}
/**将url的传参放入map*/
function setMap(){
	var url = location.search;
	if (url.indexOf("?") != -1) {
		url = url.substring(1,url.length);
		var params = url.split("&");
		for(var i = 0; i < params.length; i++){
			var key = params[i].split("=")[0];
			var value = params[i].split("=")[1];
			map[key] = value;
		}
	}
}

/**
 * 将tree转化成select所需的list
 * 返回list
 * data:treeData
 * treeId:tree某节点下属子节点
 * */
function treeToSelect(pdata,treeId,flag){
//	json对象赋值是指向存储地址，并不能直接赋值
//	var sdata = pdata;
	var sdata = newJson(pdata);
	if(treeId == undefined || flag != undefined){
		for(var i = 0; i < sdata.length; i++){
			sdata[i].content = "";
			sdata[i].id = sdata[i].value; 
			sdata[i].value = sdata[i].title;
		}
	}else {
		var isPass = false;
		for(var i = 0; i < sdata.length; i++){
			if(sdata[i].value == treeId){
				if(sdata[i].content != undefined){
					sdata = treeToSelect(sdata[i].content,treeId,true);
					isPass = true;
				}
			}
		}
		if(!isPass){
			var subData;
			for(var i = 0; i < sdata.length; i++){
				if(sdata[i].content != undefined){
					subData = treeToSelect(sdata[i].content,treeId);
				}
				if(subData != undefined){
					sdata = subData
					break;
				}
			}
		}
	}
	return sdata;
}
/*从一个json对象赋值到另一json对象*/
function newJson(data){
	var str = JSON.stringify(data);
	var newData = $.parseJSON(str);
	return newData;
}
/*时间戳转化yyyy-MM-dd*/
function getLocalTime(seconds,type) { 
	if(seconds == undefined){
		return "";
	}
    var date = new Date(seconds);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var second = date.getSeconds();
    if(month < 10){
    	month = "0"+month;
    }
    if(day < 10){
    	day = "0"+day;
    }
    if(type == 'yyyy-MM-dd'){
    	return year+"-"+month+"-"+day;
    }
    else if(type == 'yyyy/MM/dd'){
    	return year+"/"+month+"/"+day;
    }
    else if(type == 'yyyy-MM-dd HH:mm:ss'){
    	return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+secord;
    }
    else if(type == 'yyyy年MM月dd日 HH:mm:ss'){
    	return year+"年"+month+"月"+day+"日 "+hour+":"+minute+":"+secord;
    }
    else if(type == 'yyyy年MM月dd日'){
    	return year+"年"+month+"月"+day+"日";
    }
    else{
    	return year+"-"+month+"-"+day;
    }
 } 

/**全文检索并以高光显示*/
function searchText(textId){
	highLight(textId);
}
function highLight(textId){
	var searchText = $('#'+textId).val();
	var regExp = new RegExp(searchText, 'g');//全文检索
	clearSelection();
	$('font[class="ch_title"]').each(function () {
	 	var html = $(this).html();
	 	var newHtml = html.replace(regExp, '<span class="highlight">' + searchText + '</span>');
	 	$(this).html(newHtml);
	 	if(html.search(searchText) == -1){
	 		return;
	 	}
	 	if($(this).parent().parent().attr('class') == "checkbox_cell"){
	 		var checkbox_cell_div = $(this).parent().parent().parent();
	 		if(!checkbox_cell_div.hasClass("in")){
	 			checkbox_cell_div.addClass("in");
	 			var id = checkbox_cell_div.attr("id");
	 			$("#a"+id).addClass="collapsed";
	 			changeText($("#a"+id)[0]);
	 			$("#a"+id).attr("aria-expanded","true");
	 		}
	 	}
 	});
}
function clearSelection() {
 	$('font[class="ch_title"]').each(function () {
	 	$(this).find('.highlight').each(function () {
	 		$(this).replaceWith($(this).html());
	 	});
 	});
}
//预览图片
function review(targetId,reviewId){
	var docObj=document.getElementById(targetId);
	var imgObjPreview=document.getElementById(reviewId);
	if(!checkFile(targetId,"photo")){
		return;
	}
	//files属性：返回一个 Files 集合，由指定文件夹中包含的所有 File 对象组成，包括设置了隐藏和系统文件属性的文件。
	if(docObj.files && docObj.files[0]){
		imgObjPreview.src = window.URL.createObjectURL(docObj.files[0]);
	}else{
		//IE下，使用滤镜
		docObj.select();
		var imgSrc = document.selection.createRange().text;
		//图片异常的捕捉，防止用户修改后缀来伪造图片    
		try{
			imgObjPreview.style.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
			imgObjPreview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;
		}
		catch(e)
		{
			$("#"+targetId).val("");
			alert("您上传的图片格式不正确，请重新选择!");
		}
		document.selection.empty();
	}
}