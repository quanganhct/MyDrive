$(function() {

			var remote = {options:{
				loadUrl:"/uploaded/files/",
				uploadUrl:"/upload?uuid=",
				getFolderJson:"/folder/get",
				saveFolderJson:"/folder/save"
			}};
			/*
getFolderJson Format : JSON (Juste convertir le text en json et le returner)
{ folder: 
	[   { id: 0, name: null, parent: null, files: [Object] },
		{ id: 1, name: 'test', parent: 0, files: [] },
		{ id: 2, name: 'test2', parent: 1, files: [] },
		{ id: 3, name: 'test3', parent: 0, files: [] } 
	] 
}

 FilesInfo Format : JSON
{ data: 
 [ { files_url: 'http://google.drive/b1710232d20266c8efe0a50270c15f50',
	    files_range: '0',
	    files_size: '10000000' },
	 { files_url: 'http://google.drive/c7809b28dbdd46010611ac49963bdd43',
	    files_range: '100000000',
	    files_size: '10000000' },
	 { files_url: 'http://google.drive/fa011d985748a1dbb4c955a10eade923',
	    files_range: '200000000',
	    files_size: '10000000' },
	 { files_url: 'http://google.drive/a66a7b7c42ad56b75b2b95c10341558e',
	    files_range: '300000000',
	   	files_size: '10000000' }
  ] 
}

For every chunks upload, return in JSON: (Only one, this is just 3 examples)

{"files":[{"name":"f4a61caa6d9392e99f9ffa994b2bdaef","size":100000000,"type":"application/zip","deleteType":"DELETE","contentRange":"0","maxSize":"362923425","origin":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","token":"05fa67d233166a71790fed6ae76fcda0","url":"http://test.com:5000/uploaded/files/f4a61caa6d9392e99f9ffa994b2bdaef","deleteUrl":"http://test.com:5000/uploaded/files/f4a61caa6d9392e99f9ffa994b2bdaef"}]}

{"files":[{"name":"f09eeeb11f978685325543fcd5b2a6d7","size":100000000,"type":"application/zip","deleteType":"DELETE","contentRange":"100000000","maxSize":"362923425","origin":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","token":"05fa67d233166a71790fed6ae76fcda0","url":"http://test.com:5000/uploaded/files/f09eeeb11f978685325543fcd5b2a6d7","deleteUrl":"http://test.com:5000/uploaded/files/f09eeeb11f978685325543fcd5b2a6d7"}]}

{"files":[{"name":"59a5f7ff684f23fcc6a382e099974de6","size":62923425,"type":"application/zip","deleteType":"DELETE","contentRange":"300000000","maxSize":"362923425","origin":"[ www.Cpasbien.pw ] Arrow.S03E13.FASTSUB.VOSTFR.HDTV.XviD-ATN.avi.zip","token":"05fa67d233166a71790fed6ae76fcda0","url":"http://test.com:5000/uploaded/files/59a5f7ff684f23fcc6a382e099974de6","deleteUrl":"http://test.com:5000/uploaded/files/59a5f7ff684f23fcc6a382e099974de6"}]}
			*/


				remote.getAllFiles = function(files_token,callback){
					$.get('allfiles',{files_token:files_token},function(result){


							console.log(result);
							 app.load(result.data,0,function(resultLoad){
								
									console.log(resultLoad);
									var url = app.fso.toURL('test2.zip');
									$.fileDownload(url);
									app.fso.createQueue().rm('test2.zip').execute();
							});
							
							return callback(result);
					});
				}







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
						
					remote.getAllFiles($(this).attr('href'),function(e){
						console.log("object loaded");
					});
				});

				/*
				$('body').on('click','.xe-file',function(e){
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
*/

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
						folder.setCurrent(current.parent);
						console.log(folder.getCurrent());
				});

				$(".closeCreateFolder").on('click',function(e){
					var name = $('#folder-name').val();
					if(name == ""){
						name ="folder";
					}
					console.log("NAME: "+name);
					folder.createFolder(name,function(){
				   			$('#modal-folder').modal("hide");	
				   });
				})

				$('body').on('click','.supprimer',function(e){
					e.preventDefault();
					folder.saveFolderJson(function(e){

					});
				});

///////////////////////////////////////////////////
/// Folder System Script  ///
//////////////////////////////////////////////////
				var folder = {
					structure : null,
					current : null
				}

				folder.getStructure = function(){
					var data = [];
					for (var i = 0; i < folder.structure.length; i++) {
						data.push(folder.structure[i]);
					};
					return data;
				}

				folder.setStructure = function(object){
					folder.structure = object;
				}
				folder.setCurrent = function(key){
					folder.findElementByKey(key,function(e){
						//console.log(folder);
						folder.current = e;
					})
				}
				folder.getCurrent = function(){
					return folder.current;
				}

				folder.getHigherId = function(callback){
					findElementRecurrent = function(currentStructure,result,callback){
						var data = currentStructure;
						if(data.length == 0){
							return callback(result);
						}
						var object = data[0];
						if(object.id > result ){
							result = object.id;
							
						}
							data.shift();
							findElementRecurrent(data,result,callback);
						
					}
					
					 findElementRecurrent(folder.getStructure(),0,function(e){
						//console.log("Result By Key");
						//console.log(e);
						return callback(e);
					});
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
						//console.log(e);
						return callback(e);
					});
				}

				folder.displayFolders = function(key){
					folder.findChildren(key,function(e){
						var render = tmpl("template-folder")({files:e});
						$(".files").append(render);
					});
				}

				folder.createFolder = function(name,callback){
					var current = folder.getCurrent();
					console.log("current");
					console.log(current);
					folder.getHigherId(function(e){
						folder.structure.push({ id: e+1, name: name, parent: current.id, files: [] });
						folder.saveFolderJson(function(e){
							return callback(true);
						})
					})
					
				}

				folder.displayFiles = function(key){
					folder.findElementByKey(key,function(e){
						//console.log("displayFiles");
						//console.log(e);
						if(e.files.length == 0){
							return;
						}
						var render = tmpl("template-file")({files:e.files});
						//console.log(render);
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
				folder.saveFolderJson = function(callback){
					$.get(remote.options.saveFolderJson,{data:JSON.stringify({folder:folder.getStructure()})},function(e){
						return callback(e);
					});
					
				}

				folder.getFolderJson = function(callback){
					$.get(remote.options.getFolderJson,function(e){
						console.log(e);

						folder.setStructure(e.folder);
						return callback(e);
					})
					/* Data look like : Save it like a json, it's important to not consider it like a java object but like a text when u save it and return it like a json

					{ folder: [ { id: '0', name: null, parent: null, files: [] },{ id: '1', name: "hello", parent: 0, files: [] } ] }




					*/
				}
				
				folder.init = function(){
					//folder.findElementByKey("7");
					//folder.findElementByName("Folder 2");
					folder.getFolderJson(function(e){
						folder.setCurrent(0);
						folder.displayFolders(0);
						folder.displayFiles(0);
					});
					
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
       data.url = remote.options.uploadUrl+time;
      
       

	});

	$("#fileupload").bind('fileuploaddone', function(e, data){
		 console.log("current");
         console.log(folder.getCurrent());
         console.log("fileuploadsubmit -> Transfert into the JSON Folder System");
			var current = folder.getCurrent();
			console.log(data.result);
	       for (var j = 0; j < data.result.files.length; j++) {
	   			current.files.push(data.result.files[j]);
	   			console.log("Data pushed");
	   		};
	   		console.log(folder.structure);
	   		folder.saveFolderJson(function(e){
	   			alert("Json Folder Saved");
	   		})
	});
	
	$('#fileupload').bind('fileuploaddestroy', function (e, data) {
		/* ... */
		console.log("DELETE");
		console.log(data);
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
					//var node = $.ui.fancytree.getNode(ui.target);
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