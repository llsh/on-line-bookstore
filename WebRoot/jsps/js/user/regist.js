$(function(){
	/*
	 * 1.
	 */
	$(".errorClass").each(function(){
		showError($(this));
	});
	/*
	 * 2.注册图片切换
	 */
	$("#submitBtn").hover(
		function(){
			$("#submitBtn").attr("src","/goods/images/regist2.jpg");
		},
		function(){
			$("#submitBtn").attr("src","/goods/images/regist1.jpg");
		}
 	);
	/*
	 * 3.获得焦点不显示错误信息
	 */
	$(".inputClass").focus(
			function(){
				//attr()得到相应的属性值ֵ
				var labelId = $(this).attr("id")+"Error";
				$("#" + labelId).text("");
				showError($("#" + labelId));
			}
	);
	/*
	 * 4.失去焦点，执行对应的方法
	 */
	$(".inputClass").blur(
			function(){
				//获得id
				var id = $(this).attr("id");
				//获得执行的方法名
				var funName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1) + "()";
				//ִ执行方法
				eval(funName);
			}
	);
}
);
/*
 * 验证用户名
 */
function validateLoginname(){
	var id = "loginname";
	var value = $("#" + id).val();
	/*
	 * 1.非空校验
	 */
	if(!value){
		$("#" + id + "Error").text("用户名不能为空!");
		showError($("#" + id + "Error"));
//		showError($("#"  + "tdError"));
		return false;
	}
	/*
	 * 2.长度校验
	 */
	if(value.length < 3 || value.length > 20){
		$("#" + id + "Error").text("用户名必选在3到20之间!");
		showError($("#" + id + "Error"));
		return false;
	}
	/*
	 * 3.是否已经注册
	 */
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateLoginname",loginname:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				//校验失败
				$("#" + id + "Error").text("用户名已经被注册");
				showError($("#" + id + "Error"));
				return false;
			}
		}
	});
	return true;
}

/*
 * 检验密码
 */
function validateLoginpass(){
	var id = "loginpass";
	var value = $("#" + id).val();
	/*
	 * 1.非空校验
	 */
	if(!value){
		$("#" + id + "Error").text("密码不能为空!");
		showError($("#" + id + "Error"));
		return false;
	}
	/*
	 * 2.长度校验
	 */
	if(value.length < 3 || value.length > 20){
		$("#" + id + "Error").text("用户名必选在3到20之间!");
		showError($("#" + id + "Error"));
		return false;
	}

	return true;
}
/*
 *确认密码校验
 */
function validateReloginpass(){
	var id = "reloginpass";
	var value = $("#" + id).val();
	/*
	 * 1.非空校验
	 */
	if(!value){
		$("#" + id + "Error").text("确认密码不能为空!");
		showError($("#" + id + "Error"));
		return false;
	}
	/*
	 * 2.两次密码校验
	 */
	if($("#loginpass").val() != value){
		$("#" + id + "Error").text("两次密码输入不一致!");
		showError($("#" + id + "Error"));
		return false;
	}

	return true;
}
/*
 * 邮箱验证
 */
function validateEmail(){
	var id = "email";
	var value = $("#" + id).val();
	/*
	 * 1.非空校验
	 */
	if(!value){
		$("#" + id + "Error").text("邮箱不能为空!");
		showError($("#" + id + "Error"));
		return false;
	}
	/*
	 * 2.邮箱格式验证
	 */
	if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)){
		$("#" + id + "Error").text("邮箱格式不对!");
		showError($("#" + id + "Error"));
		return false;
	}

	/*
	 * 3.邮箱是否已注册
	 */
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateEmail", email:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				//校验失败
				$("#" + id + "Error").text("邮箱已经被注册");
				showError($("#" + id + "Error"));
				return false;
			}
		}
	});
	return true;
}
/*
 *验证码校验
 */
function validateVerifyCode(){
	var id = "verifyCode";
	var value = $("#" + id).val();
	//1.非空校验	
	if(!value){
		var lableId = id + "Error";
		$("#" + lableId).text("验证码不能为空！");
		showError($("#" + lableId));
		return false;
	}
	//2.长度校验
	if(value.length != 4){
		var lableId = id + "Error";
		$("#" + lableId).text("验证码长度为4位！");
		showError($("#" + lableId));
		return false;
	}
	//3.是否正确校验
	$.ajax({
		url:"/goods/UserServlet",
		data:{method:"ajaxValidateCode",verifyCode:value},
		type:"POST",
		dataType:"json",
		async:false,
		cache:false,
		success:function(result){
			if(!result){
				//校验失败
				$("#" + id +"Error").text("验证码不对！");
				showError($("#" + id + "Error"));
				return false;
			}
		}
	});
}

function showError(ele){
	var text = ele.text();
	if(!text){
		//没有错误不显示
		ele.css("display", "none");
	}else {
		//有错误显示
		ele.css("display", ""); 
	}
}	

function _hyz(){
	/*
	 *换图片的异步请求
	 */
	$("#imgCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}


