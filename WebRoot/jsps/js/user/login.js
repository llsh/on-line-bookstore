$(function() {
	/*
	 * 1. 切换登陆按钮
	 */
	$("#submit").hover(
		function() {
			$("#submit").attr("src", "/goods/images/login2.jpg");
		},
		function() {
			$("#submit").attr("src", "/goods/images/login1.jpg");
		}
	);
	
	/*
	 * 2. 提交表单时进行一次校验
	 */
	$("#submit").submit(function(){
		$("#msg").text("");
		var bool = true;
		$(".input").each(function() {
			var inputName = $(this).attr("name");
			if(!invokeValidateFunction(inputName)) {
				bool = false;
			}
		});
		return bool;
	});
	
	/*
	 * 3. 点击文本框不显示错误
	 */
	$(".input").focus(function() {
		var inputName = $(this).attr("name");
		$("#" + inputName + "Error").css("display", "none");
	});
	
	/*
	 * 4. 光标离开，校验文本框
	 */
	$(".input").blur(function() {
		var inputName = $(this).attr("name");
		invokeValidateFunction(inputName);
	})
});

/*
 * 对输入的数据进行校验，并执行相对应的方法
 */
function invokeValidateFunction(inputName) {
	inputName = inputName.substring(0, 1).toUpperCase() + inputName.substring(1);
	var functionName = "validate" + inputName;
	return eval(functionName + "()");	
}

/*
 * 校验登陆名
 */
function validateLoginname() {
	var bool = true;
	$("#loginnameError").css("display", "none");
	var value = $("#loginname").val();
	if(!value) {
		$("#loginnameError").css("display", "");
		$("#loginnameError").text("用户名不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {
		$("#loginnameError").css("display", "");
		$("#loginnameError").text("用户名长度必须在3到20之间！");
		bool = false;
	}
	return bool;
}

/*
 * 校验密码
 */
function validateLoginpass() {
	var bool = true;
	$("#loginpassError").css("display", "none");
	var value = $("#loginpass").val();
	if(!value) {
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码不能为空！");
		bool = false;
	} else if(value.length < 3 || value.length > 20) {
		$("#loginpassError").css("display", "");
		$("#loginpassError").text("密码长度必须在3到20之间");
		bool = false;
	}
	return bool;
}

/*
 * 验证码校验
 */
function validateVerifyCode() {
	var bool = true;
	$("#verifyCodeError").css("display", "none");
	var value = $("#verifyCode").val();
	if(!value) {
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("验证码不能为空！");
		bool = false;
	} else if(value.length != 4) {
		$("#verifyCodeError").css("display", "");
		$("#verifyCodeError").text("验证码为4位");
		bool = false;
	} else {
		$.ajax({
			cache: false,
			async: false,
			type: "POST",
			dataType: "json",
			data: {method: "ajaxValidateCode", verifyCode: value},
			url: "/goods/UserServlet",
			success: function(flag) {
				if(!flag) {
					$("#verifyCodeError").css("display", "");
					$("#verifyCodeError").text("验证码错误！");
					bool = false;					
				}
			}
		});
	}
	return bool;
}

function _hyz1(){
	/*
	 *换图片的异步请求
	 */
	$("#vCode").attr("src","/goods/VerifyCodeServlet?a="+new Date().getTime());
}
