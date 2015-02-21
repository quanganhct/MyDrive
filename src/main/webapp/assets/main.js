$(function() {
			var eventObject = {currentObject:null,numberOfClick:0,currentAction:null}

				eventObject._checkIfSame = function(object){
					if(eventObject.currentObject == object){
						return true;
					}else{
						return false;
					}
				}
				eventObject.setEvent = function(object,action){
					if(eventObject._checkIfSame(object)){
						eventObject.numberOfClick++;
						eventObject.currentAction=action;
					}else{
						eventObject.currentObject=object;
						eventObject.numberOfClick=0;
						eventObject.currentAction=action;
					}
				}

				eventObject.cleanAll = function(){
					$(".template-download .xe-vertical-counter-blue").each(function(index, el) {

						if($(el).hasClass('xe-file')){
							$(this).addClass("xe-vertical-counter-white");
						}else if($(el).hasClass('xe-folder')){
							$(this).addClass("xe-vertical-counter-warning");
						}

						$(this).removeClass("xe-vertical-counter-blue");
					});
				}

				/*
				eventObject.checkIfSameAction = function(object,action){
					if(action != eventObject.currentAction){
						eventObject.cleanAll();
					}
				}
				*/

				$('body').on('click','.downloadFile',function(e){
					e.preventDefault();
					eventObject.setEvent($(this),'download');
					console.log($(this).attr('href'));
					//console.log(new Date());
					//console.log(moment().format("x"));
					$.get('allfiles',{files_token:$(this).attr('href')},function(result){


							console.log(result);
							/* app.load(result.data,0,function(resultLoad){
								
									console.log(resultLoad);
									var url = app.fso.toURL('test2.zip');
									$.fileDownload(url);
									app.fso.createQueue().rm('test2.zip').execute();
							});
							*/
					});
				});

				$('body').on('click','.xe-file',function(e){
					//console.log(e);
					e.preventDefault();
					eventObject.setEvent($(this),'select');

					console.log("Click on template");
					if($(this).hasClass('xe-vertical-counter-white')){
						$(this).removeClass("xe-vertical-counter-white");
						$(this).addClass("xe-vertical-counter-blue");

					}else{
						$(this).addClass("xe-vertical-counter-white");
						$(this).removeClass("xe-vertical-counter-blue");
					}
					
				});

				$('body').on('click','.xe-folder',function(e){
						e.preventDefault();
						eventObject.setEvent($(this),'open');
					console.log("Open Folder");

					var key = $(this).data('key');
						eventObject.cleanAll();

						app.cleanDrop();
						folder.displayMain(key);
						folder.displayFolders(key);
						folder.displayFiles(key);

				});

				$('body').on('click','.xe-main',function(e){
						e.preventDefault();
						eventObject.setEvent($(this),'return');
					console.log("Return in parent folder");

					var key = $(this).data('key');
						eventObject.cleanAll();

						app.cleanDrop();
						var current = folder.getCurrent();
						folder.displayMain(current.parent);
						folder.displayFolders(current.parent);
						folder.displayFiles(current.parent);

				});



				$('body').on('click','.supprimer',function(e){
					e.preventDefault();
					eventObject.setEvent($(this),'delete');
					console.log("DELETE");
					app.delete();
				});

///////////////////////////////////////////////////
/// Folder System Script  ///
//////////////////////////////////////////////////
				var folder = {
					structure : [{id:"0",name:null,parent:null,files:[]},{id:"1",name:"Folder 1",parent:"0",files:[{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"c831a59a3c5fae048399ae750b315d08","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"},{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"16c1da1a265253103ae4e46ba16a0f40","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"},{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"a55c1c0bf8c49a5e7d7f02e9aa1e1461","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"}]},{id:"2",name:"Folder 2",parent:"0",files:[]},{id:"3",name:"Folder 3",parent:"1",files:[]},{id:"4",name:"Folder 4",parent:"1",files:[]},{id:"5",name:"Folder 5",parent:"2",files:[]},{id:"6",name:"Folder 2",parent:"2",files:[{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"c831a59a3c5fae048399ae750b315d08","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"},{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"16c1da1a265253103ae4e46ba16a0f40","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"},{"name":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","size":"40000000","modified":null,"deleteType":"DELETE","contentRange":null,"maxSize":null,"origin":null,"token":"a55c1c0bf8c49a5e7d7f02e9aa1e1461","url":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","deleteUrl":"http://test.com:5000/uploaded/files/%5B%20www.Cpasbien.pw%20%5D%20Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip"}]},{id:"7",name:"Folder 7",parent:"2",files:[]}],
					current : null
				}

				folder.getStructure = function(){
					var data = [];
					for (var i = 0; i < folder.structure.length; i++) {
						data.push(folder.structure[i]);
					};
					return data;
				}
				folder.setCurrent = function(key){
					folder.findElementByKey(key,function(e){
						console.log(folder);
						folder.current = e;
					})
				}
				folder.getCurrent = function(){
					return folder.current;
				}
				folder.setChildren = function(object,child){

				}
				folder.getChildren = function(object){
					return object.children;
				}
				folder.findElementByKey = function(key,callback){
					findElementRecurrent = function(key,currentStructure,result,callback){
						var data = currentStructure;
						if(data.length == 0){
							return callback(result);
						}
						var object = data[0];
						if(object.id == key){
							result = object;
							return callback(result);
						}
							data.shift();
							findElementRecurrent(key,data,result,callback);
						
					}
					
					 findElementRecurrent(key,folder.getStructure(),null,function(e){
						//console.log("Result By Key");
						//console.log(e);
						return callback(e);
					});
				}
				folder.findElementByName = function(key,callback){
					findElementRecurrent = function(key,currentStructure,result,callback){
						var data = currentStructure;
						if(data.length == 0){
							return callback(result);
						}
						var object = data[0];
						//console.log(object);
						if(object.name == key){
							result.push(object);
						}

						data.shift();
						findElementRecurrent(key,data,result,callback);
						
					}
					findElementRecurrent(key,folder.getStructure(),[],function(e){
						//console.log("Result By Name");
						//console.log(e);
						return callback(e);
					});
				}
				folder.findChildren = function(parent,callback){

					findChildrenRecurrent = function(parent,currentStructure,result,callback){
						var data = currentStructure;
						if(data.length == 0){
							return callback(result);
						}
						var object = data[0];
						//console.log(object);
						if(object.parent == parent){
							result.push(object);
						}

						data.shift();
						findChildrenRecurrent(parent,data,result,callback);
						
					}

					findChildrenRecurrent(parent,folder.getStructure(),[],function(e){
						console.log("Result Children");
						console.log(e);
						return callback(e);
					});
				}

				folder.displayFolders = function(key){
					folder.findChildren(key,function(e){
						var render = tmpl("template-folder")({files:e});
						$(".files").append(render);
					});
				}

				folder.displayFiles = function(key){
					folder.findElementByKey(key,function(e){
						console.log("displayFiles");
						console.log(e);
						var render = tmpl("template-download")({files:e.files});
						console.log(render);
						$(".files").append(render);
					});
				}

				folder.displayMain = function(key){
					if(key == 0)
						return;
					folder.findElementByKey(key,function(e){
						var data = [];
							data.push(e);
						var render = tmpl("template-main")({files:data});
						$(".files").append(render);
						folder.setCurrent(key);
					});
						
				}
				
				folder.init = function(){
					//folder.findElementByKey("7");
					//folder.findElementByName("Folder 2");
					
					folder.setCurrent(0);
					folder.displayFolders(0);
					folder.displayFiles(0);
				}



///////////////////////////////////////////////////
/// Event Script  ///
//////////////////////////////////////////////////


 var listOfFiles;
    // Initialize the jQuery File Upload widget:
    $('#fileupload').fileupload({
        url: "/upload",
        dataType: 'json',
        maxFileSize: 50000000000,
        maxChunkSize: 100000000,
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png|zip)$/i,
        autoUpload:true
        /*submit: function (e, data) {
	        console.log(data);
	        return false;
	    } */
        /*add: function (e, data) {
                    alert(data.files);
        }*/
    });
    
    $('#fileupload').bind('fileuploadsubmit', function (e, data) {
       console.log(moment().format("x"));
       var time = moment().format("x");
       data.url = "/upload?uuid="+time;
	   
	});
	
	$('#fileupload').bind('fileuploaddestroy', function (e, data) {
		/* ... */
		console.log("DELETE");
		console.log(data);
	});

      $('#fileupload').addClass('fileupload-processing');
        $.ajax({
            // Uncomment the following to send cross-domain cookies:
            //xhrFields: {withCredentials: true},
            url: $('#fileupload').fileupload('option','url'),
            dataType: 'json',
            context: $('#fileupload')[0]
        }).always(function () {
            $(this).removeClass('fileupload-processing');
        }).done(function (result) {
          //alert($('#fileupload')[0]);
          listOfFiles = result;
          app.run();
            //$(this).fileupload('option', 'done').call(this, $.Event('done'), {result: result});
        });


        $(".files").contextmenu({
				delegate: ".xe-folder",
				// menu: "#options",
				menu: [
					{title: "Cut", cmd: "cut", uiIcon: "ui-icon-scissors",action: function(event, ui){
							alert("Cut");
						}
					},
					{title: "Copy", cmd: "copy", uiIcon: "ui-icon-copy"},
					{title: "Paste", cmd: "paste", uiIcon: "ui-icon-clipboard", disabled: false },
					{title: "----"},
					{title: "Edit", cmd: "edit", uiIcon: "ui-icon-pencil", disabled: true },
					{title: "Delete", cmd: "delete", uiIcon: "ui-icon-trash", disabled: true },
					{title: "More", children: [
					{title: "Sub 1", cmd: "sub1"},
					{title: "Sub 2", cmd: "sub1"}
					]}
				],
				beforeOpen: function(event, ui) {
					console.log("before open");
				},
				select: function(event, ui) {
					var node = $.ui.fancytree.getNode(ui.target);
					//alert("select " + ui.cmd + " on " + node);
				}
			});
///////////////////////////////////////////////////
/// App Script  ///
//////////////////////////////////////////////////

		var app ={
			fso: null,
			fsq:null,
			data : [],
			cleanDrop : function(){
				$('.files').html("");
			},
			delete : function(){
				$(".template-download .xe-vertical-counter-blue").each(function(index, el) {
					if($(el).hasClass('xe-file')){
						$(el).parent().find(".delete").trigger("click");
						console.log("Click: "+index);
					}
				});
			},
			load: function(data,num,callback){
				console.log("LOAD: "+num);
				if(data.length == 0){
					callback(true);
				}else{
					var oReq = new XMLHttpRequest();
					oReq.open("GET", "/uploaded/files/"+data[0].files_url, true);
					oReq.responseType = "arraybuffer";

					oReq.onload = function(oEvent) {
						console.log(oEvent);
					  //var blob = new Blob([oReq.response], {type: "application/zip"});
					  //var object = {name:data[0].files_range,data:oReq.response,type:blob.type,size:blob.size}
					  //delete blob;
					  data.shift();
					 
					  app.fsq = app.fso.createQueue();
					  console.log("FSQ created");
					  console.log(app.fso);
					  console.log(app.fsq);
					  if(num == 0){
					  	 app.fsq.write(
							'test2.zip',
							oReq.response,
							function() { console.log('wrote to file');
						}).execute(function(){
							console.log("execute :"+num);
								var nextNum = num +1 ;
								setTimeout(function(){
									app.load(data,nextNum,callback);
								},500);
								
						});
							
					  }else{
					  	 app.fsq.append(
							'test2.zip',
							oReq.response,
							function() { console.log('Append to file');
						}).execute(function(){
							console.log("execute :"+num);
								var nextNum = num +1 ;
								setTimeout(function(){
									app.load(data,nextNum,callback);
								},500);
						});
					  }
					  
					  //result.push(object);
					  
					  //saveAs(blob, "hello world.jpeg");
					};

					oReq.send();
				}
			},
			run: function(){
			//	app.fso = new FSO(1024 * 1024 * 1024 * 10, true);
				console.log(listOfFiles);
			}
		}

		//app.run();
		folder.init();




		});