$(function() {

			var remote = {options:{
				loadUrl:"",
				uploadUrl:"/rest/command/upload/",
				getFolderJson:"/rest/command/folder/get",
				saveFolderJson:"/rest/command/folder/set",
				deleteUrl:"/rest/command/delete",
				space:"/upload/size",
				allFiles:"/rest/command/allfiles"
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


				remote.getAllFiles = function(files_token,name,token,callback){
					$.get(remote.options.allFiles,{file_token:files_token},function(e){
							e = JSON.stringify(eval('('+e+')'));
							obj = JSON.parse(e);
							var result = [];
							for(el in obj){
								result.push(obj[el]);
								//console.log(obj[el]);
							}

							result.sort(function(a, b){return a.files_range-b.files_range});

							console.log(result);
							//console.log(result[1].files_range);
							//console.log(result[2].files_range);
							//console.log(result[3].files_range);
							app.loadBar.max = result.length;
							app.showLoading();
							//app.updateLoading();
							app.load(result,0,name,token,function(resultLoad){
									//app.fso = new FSO(1024 * 1024 * 1024 * 10, true);
									console.log(resultLoad);
									var url = app.fso.toURL(name);
									$.fileDownload(url);
									app.loadBar.max=0;
									app.loadBar.current=1;

									//app.fso.createQueue().rm(token).execute();
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
				$('body').on('click',"#logout",function(e){
					e.preventDefault();
					app.logout();
				})

				$('body').on('click','.downloadFile',function(e){
					e.preventDefault();
					eventObject.setEvent($(this),'download');
						
					var name = $(this).data("name");
					var token = $(this).data("token");
						console.log(name);
						console.log("Adress: "+$(this).attr('href'));
					var adress = $(this).attr('href');
					remote.getAllFiles(adress,name,token,function(e){

						console.log(adress);		
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
					current : null,
					copy:{type:null,object:null}
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

				folder.extractFile = function(token,folder,callback){
					var files = folder.files;
					var result = [];
					var object = null;

					for (var i = 0; i < files.length; i++) {
						if(files[i].token != token){
							result.push(files[i]);
						}else{
							object = files[i];
						}
							
					}

					return callback({listFiles:result,file:object});
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
						var object = { id: e+1, name: name, parent: current.id, files: [] };
						folder.structure.push(object);
							var render = tmpl("template-folder")({files:[object]});
							$(".files").append(render);
							//console.log(render);
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
					$.get(remote.options.saveFolderJson,{folderJSON:JSON.stringify({folder:folder.getStructure()})},function(e){
						return callback(e);
					});
					
				}

				folder.getFolderJson = function(callback){
					$.get(remote.options.getFolderJson,function(e){
						//console.log(e);
						e = JSON.stringify(eval('('+e+')'));
						var json = JSON.parse(e);

						folder.setStructure(json.folder);
						//console.log(folder.structure);
						return callback(e);
					})
					/* Data look like : Save it like a json, it's important to not consider it like a java object but like a text when u save it and return it like a json

					{ folder: [ { id: '0', name: null, parent: null, files: [] },{ id: '1', name: "hello", parent: 0, files: [] } ] }




					*/
				}

				folder.removeFileOfArray = function(files,token,callback){
					var result = [];
					for (var i = 0; i < files.length; i++) {
						if(files[i].token != token){
							result.push(files[i]);
						}
					};
					console.log("RemoveFileOfArray");
					callback(result);
				}

				folder.removeFile = function(token,callback){
					var current = folder.getCurrent();
					// To do again
					
						for (var j = 0; j < current.files.length; j++) {
							console.log(current.files[j].token+" == "+token);
							if(current.files[j].token == token){

								folder.removeFileOfArray(current.files,token,function(e){
									console.log("File removed");
									current.files=e;
								});
							}
							
						}
						
			
					return callback();
				}

				/*
				folder.removeFileByFolder = function(folder,callback){
				
						var listToken = [];
						for (var j = 0; j < folder.files.length; j++) {
							listToken.push(folder.files[j].token);

							
							
						}
						folder.files = [];
			
						return callback(listToken);
						
				}
				folder.removeOneFolder = function(key,callback){
					var result = [];
					var structure = folder.structure;

					for (var i = 0; i < structure.length; i++) {
						if(key != structure[i].id){
							result.push(structure[i]);
						}
					}
			
					return callback(result);
				}
				*/

				folder.getAllChildrenOfKey = function(key,callback){

					arrayConcat = function(arr1,arr2){
						for (var i = 0; i < arr2.length; i++) {
							arr1.push(arr2[i]);
						};
						return arr1;
					}

					arrayOfToken = function(arr){
						var result = [];
						for (var i = 0; i < arr.length; i++) {
							result.push(arr[i].token);
						};
						return result;
					}

					returnChildren = function(listFolder,resultFolder,resultFile,callback){
						if(listFolder.length == 0)
							return callback({resultFolder:resultFolder,resultFile:resultFile});
						// Put the iD of the folder
						resultFolder.push(listFolder[0].id);
						resultFile = arrayConcat(resultFile,arrayOfToken(listFolder[0].files));
						// Check the actual folder
						folder.findChildren(listFolder[0].id,function(childrens){
							listFolder = arrayConcat(listFolder,childrens)
							listFolder.shift();
							//console.log("resultFolder size: "+resultFolder.length);
							//console.log("resultFile size: "+resultFile.length);
							//console.log({resultFolder:resultFolder,resultFile:resultFile});
							returnChildren(listFolder,resultFolder,resultFile,callback);
						});
						

					}

					folder.findElementByKey(key,function(e){
						returnChildren([e],[],[],function(f){
							return callback(f);
						});
					});


				}


				folder.removeFolder = function(arr,callback){
					
					arrayOfId = function(arr){
						var result = [];
						for (var i = 0; i < arr.length; i++) {
							result.push(arr[i].id);
						};
						return result;
					}

					arrContain = function(object,arr){
						for (var i = 0; i < arr.length; i++) {
							if(object == arr[i])
								return true;
						};
						return false;
					}

					var structure = folder.structure;
					var structureID = arrayOfId(folder.structure);
					var result = [];

					for (var i = 0; i < structure.length; i++) {
						if(!arrContain(structure[i].id,arr)){
							result.push(structure[i]);
						}else{
							console.log("One Folder Removed");
						}
					}
					folder.structure = result;
					return callback(result);

					
				}

				/*
				folder.moveFile = function(origin,destination,callback){ // function({file:file,folder:folder},destination:folder)
					var file =origin.file;
					var folder = origin.folder;

					folder.removeFileOfArray(folder.files,file,function(e){
						folder.files = e;
						destination.files.push(file);
						return callback();
					});

					


				}
				*/
				
				folder.init = function(){
					//folder.findElementByKey("7");
					//folder.findElementByName("Folder 2");
					folder.getFolderJson(function(e){
						folder.setCurrent(0);
						folder.displayFolders(0);
						folder.displayFiles(0);
					});
					app.getSpace();
					
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
	   			//alert("Json Folder Saved");
	   		})
	});
	
	$('#fileupload').bind('fileuploaddestroy', function (e, data) {
		/* ... */
		console.log("DELETE");
		console.log(data);
	});
	
		/*
		*
		*		MENU for the files and folders
		*
		*/

        $(".files").contextmenu({
				delegate: ".xe-action",
				// menu: "#options",
				menu: [
					{title: "Cut", cmd: "cut", uiIcon: "ui-icon-copy", action: function(event,ui){
							var target = ui.target.closest('.xe-widget');
							//console.log(target);
							if(target.hasClass('xe-folder')){

							}else if(target.hasClass('xe-file')){
								var token = target.parent().find('.downloadFile').data('token');
								console.log(token);
								folder.extractFile(token,folder.getCurrent(),function(e){
									console.log(e);
									folder.getCurrent().files = e.listFiles;
									folder.copy.type = "file";
									folder.copy.object = e.file;
									target.parent().remove();
								});

								
							}
						}
					},
					{title: "Paste", cmd: "paste", uiIcon: "ui-icon-clipboard", action: function(event,ui){
						if(folder.copy.object == null || folder.copy.type == null)
								return ;


							var target = ui.target.closest('.xe-widget');

							if(target.hasClass('xe-folder')){
								var key = target.data('key');
								//console.log("Key: "+key);
								

								folder.findElementByKey(key,function(e){
									e.files.push(folder.copy.object);
									folder.copy.type = null;
									folder.copy.object = null;
									folder.saveFolderJson(function(e){
							   			//var render = tmpl("template-file")({files:[pasteObject.object]});
										//$(".files").append(render);
							   		})
								});
							}


						}
					},
					{title: "----"},
					{title: "Delete", cmd: "delete", uiIcon: "ui-icon-trash",action: function(event,ui){
							var target = ui.target.closest('.xe-widget');

							if(target.hasClass('xe-folder')){
								var key = target.data('key');
								console.log("Key: "+key);
								folder.getAllChildrenOfKey(key,function(e){
									folder.removeFolder(e.resultFolder,function(){
										folder.saveFolderJson(function(g){
											if(e.resultFile.length != 0){
												app.delete(e.resultFile,function(f){
														target.parent().remove();
														console.log("FOLDER REMOVED");
												});
											}else{
												target.parent().remove();
												console.log("FOLDER REMOVED");
											}
												
										});
									})
								});

							}else if(target.hasClass('xe-file')){
								var token = target.parent().find('.downloadFile').data('token');
								console.log(token);
								folder.removeFile(token,function(e){
									app.delete([token],function(f){
										folder.saveFolderJson(function(e){
											target.parent().remove();
											console.log("Remove is a success");
											console.log(folder.structure);
										});
									});
									
								})
							}
						} 
					}
				],
				beforeOpen: function(event, ui) {
					//console.log("before open");
					console.log(ui);
				},
				select: function(event, ui) {
					//console.log(ui);
					//var node = $.ui.fancytree.getNode(ui.target);
					//alert("select " + ui.cmd + " on " + node);
				}
			});

		/*
		*
		*		MENU for the empty area
		*
		*/

		var contextmenu = $("body").contextmenu({
				delegate: ".files",//delegate: ".xe-action",
				// menu: "#options",
				menu: [
					/*{title: "Cut", cmd: "cut", uiIcon: "ui-icon-scissors",action: function(event, ui){
							alert("Cut");
						}
					},
					
					{title: "Copy", cmd: "copy", uiIcon: "ui-icon-copy"},
					*/
					{title: "Paste", cmd: "paste", uiIcon: "ui-icon-clipboard", action: function(event,ui){
						if(folder.copy.object == null || folder.copy.type == null)
								return ;

							var current = folder.getCurrent();

							if(folder.copy.type == "file"){
								current.files.push(folder.copy.object);

								folder.saveFolderJson(function(e){
						   			var render = tmpl("template-file")({files:[folder.copy.object]});
									$(".files").append(render);
									
									folder.copy.type = null;
									folder.copy.object = null;
						   		})
							}
						}
					},
					/*
					{title: "----"},
					{title: "Edit", cmd: "edit", uiIcon: "ui-icon-pencil", disabled: true },*/
				
					/*{title: "More", children: [
					{title: "Sub 1", cmd: "sub1"},
					{title: "Sub 2", cmd: "sub1"}
					]}*/
				],
				beforeOpen: function(event, ui) {
					var target = ui.target.closest('.xe-widget');
					if(target.hasClass('xe-folder') || target.hasClass('xe-file') || target.hasClass('xe-main') )
						return false;

					//console.log("Click on empty area");
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
			loadBar :{max:5,current:1},
			data : [],
			logout:function(){
				$.get("/rest/command/logout",function(){
					window.location="login";
				})
			},
			getSpace : function(){
				$.get(remote.options.space,function(e){
					$("#sizeUsed").html(e.message);
					$("#barSizeUsed").css("width",e.size+"%");
				})
			},
			cleanDrop : function(){
				$('.files').html("");
			},
			showLoading : function(){
		            $.blockUI({ message: $('#loader') }); 
		           //app.updateLoading();
			},
			updateLoading : function(){
				if(app.loadBar.current > app.loadBar.max)
					return;
				$('#telechargementBar').animate({
					width: (app.loadBar.current/app.loadBar.max)*100+"%",
				}, 60000, function() {
					
				});
				console.log((app.loadBar.current/app.loadBar.max)*100+"%");
				
				
				
			},
			hideLoading : function(){
					$.unblockUI();
			},
			delete : function(tokens,callback){
				if(tokens.length == 0)
					return;
				if(tokens.length == 1){
					var token = tokens[0];

					$.get(remote.options.deleteUrl+"/"+token,function(e){
						return callback(e);
					});
				}else{
					var token = tokens[0];
					tokens.shift();
					$.get(remote.options.deleteUrl+"/"+token,function(e){});
					app.delete(tokens,callback);
				}

				
				/*
				$(".template-download .xe-vertical-counter-blue").each(function(index, el) {
					if($(el).hasClass('xe-file')){
						$(el).parent().find(".delete").trigger("click");
						console.log("Click: "+index);
					}
				});
				*/


			},
			load: function(data,num,name,token,callback){
				console.log("LOAD: "+num);
				if(data.length == 0){
					app.hideLoading();
					callback(true);
				}else{
					
					var oReq = new XMLHttpRequest();
					oReq.open("GET",data[0].url+"&access_token="+data[0].files_access_token, true);

					//console.log("url: https://docs.google.com/uc?export=download&id="+data[0].id);
					oReq.responseType = "arraybuffer";

					oReq.onload = function(oEvent) {
						//console.log(oEvent);
						
						$('#telechargementBar').css("width",(app.loadBar.current/app.loadBar.max)*100+"%");
						app.loadBar.current++;
						console.log((app.loadBar.current/app.loadBar.max)*100+"%");
					  //var blob = new Blob([oReq.response], {type: "application/zip"});
					  //var object = {name:data[0].files_range,data:oReq.response,type:blob.type,size:blob.size}
					  //delete blob;
					  data.shift();
					 
					  app.fsq = app.fso.createQueue();
					  //console.log("FSQ created");
					  //console.log(app.fso);
					  //console.log(app.fsq);
					  if(num == 0){
					  	 app.fsq.write(
							name,
							oReq.response,
							function() { console.log('wrote to file');
						}).execute(function(){
							//console.log("execute :"+num);
								var nextNum = num +1 ;
								setTimeout(function(){
									app.load(data,nextNum,name,token,callback);
								},500);
								
						});
							
					  }else{
					  	 app.fsq.append(
							name,
							oReq.response,
							function() { console.log('Append to file');
						}).execute(function(){
							//console.log("execute :"+num);
								var nextNum = num +1 ;
								setTimeout(function(){
									app.load(data,nextNum,name,token,callback);
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
				app.fso = new FSO(1024 * 1024 * 1024 * 10, true);
				//app.fso.createQueue().rm(token).execute();
				console.log(listOfFiles);
			}
		}

		app.run();
		//app.showLoading();
		folder.init();




		});