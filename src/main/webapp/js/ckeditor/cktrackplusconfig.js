CKEDITOR.editorConfig = function( config )
{
	config.extraPlugins='issue,code,simpleuploads,image2,advancedlink';
	config.height = 130;
	config.toolbar =
		[
		 ['Bold','Italic','Underline','-','TextColor','BGColor','-','JustifyLeft','JustifyCenter','JustifyRight','-','NumberedList','BulletedList','-','Format','FontSize'],
		 ['Maximize','-','Advancedlink','issue','task','code','Image','Table'],
		 ['Source', 'Blockquote']
	];

	config.removePlugins = 'elementspath,contextmenu,liststyle,tabletools,link';
	config.disableNativeSpellChecker = false;
	config.resize_enabled = false;

	config.baseFloatZIndex=90001;
	config.shiftEnterMode = CKEDITOR.ENTER_BR;
	config.enterMode = CKEDITOR.ENTER_P;
	config.extraAllowedContent='a[target,type,objectid];div[workitemid,class,style]{*}(*);figure;figcaption;span{*}(*);ol[type]';
	//config.scayt_sLang = getScaytLocale();
	//config.scayt_autoStartup = true;
	config.filebrowserImageBrowseUrl = 'browseFile.action?type=Images';
	config.filebrowserBrowseUrl  = 'browseFile.action';
	config.filebrowserWindowWidth='600';
	config.filebrowserWindowHeight='400';
	//config.filebrowserImageUploadUrl ='browseFile!upload.action?type=Images';

	config.entities = false;
	config.basicEntities = true;
	config.entities_greek = false;
	config.entities_latin = false;
	config.entities_additional = '';


	var uploadUrl="browseFile!uploadFile.action";
	config.filebrowserUploadUrl = uploadUrl + '?Type=File';
	config.filebrowserImageUploadUrl = uploadUrl + '?Type=Image';

	// Restrict uploads to the extensions that are allowed in this file uploader
	config.simpleuploads_acceptedExtensions ='7z|avi|csv|doc|docx|flv|gif|gz|gzip|jpeg|jpg|mov|mp3|mp4|mpc|mpeg|mpg|ods|odt|pdf|png|ppt|pxd|rar|rtf|tar|tgz|txt|vsd|wav|wma|wmv|xls|xml|zip';
};
