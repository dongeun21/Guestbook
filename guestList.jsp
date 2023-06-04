<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <title>방명록 관리 앱</title>
</head>
<body>
<div class="container w-75 mt-5 mx-auto">
    <h2>방명록 목록</h2>
    <hr>
    <div class="table-responsive">
        <table class="table table-striped table-hover text-center">
            <thead>
            <tr>
                <th scope="col">번호</th>
                <th scope="col">작성자</th>
                <th scope="col">이메일</th>
                <th scope="col">작성일</th>
                <th scope="col">제목</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="guest" items="${guestlist}" varStatus="status">
                <tr>
                    <th scope="row">${status.count}</th>
                    <td>${guest.writer}</td>
                    <td>${guest.email}</td>
                    <td>${guest.date}</td>
                    <td>
                    	<a href="guest?action=editGuest&idx=${guest.idx}">${guest.title}</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <hr>
    <c:if test="${error != null}">
        <div class="alert alert-danger alert-dismissible fade show mt-3">
            에러 발생: ${error}
            <button type="button" class="btn-close" data-dismiss="alert"></button>
        </div>
    </c:if>

    <button class="btn btn-outline-info mb-3" type="button" onclick="location.href='guest?action=addGuest'">등록</button>
<div class="collapse" id="addForm">
    <div class="card card-body">
        <form method="post" action="guest?action=addGuest">
            <div class="mb-3">
                <label for="writer" class="form-label">작성자</label>
                <input type="text" class="form-control" id="writer" name="writer">
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">이메일</label>
                <input type="email" class="form-control" id="email" name="email">
            </div>
            <div class="mb-3">
                <label for="title" class="form-label">제목</label>
                <input type="text" class="form-control" id="title" name="title">
            </div>
            <div class="mb-3">
            	<label for="password" class="form-label">비밀번호</label>
            	<input type="text" class="form-control" id="password" name="password">
            </div>
            <div class="mb-3">
                <label for="content" class="form-label">내용</label>
                <textarea class="form-control" id="content" name="content" rows="3"></textarea>
            </div>
            <button type="submit" class="btn btn-success mt-3">입력</button>
        	<button type="reset" class="btn btn-danger" onclick="clearForm()">취소</button>
        	<button type="button" class="btn btn-secondary" onclick="goToList()">목록</button>
        </form>
    </div>
</div>
</div>

<script>
	function clearForm() {
		document.getElementById("writer").value = "";
		document.getElementById("email").value = "";
		document.getElementById("title").value = "";
		document.getElementById("password").value = "";
		document.getElementById("content").value = "";
	}
	
	function goToList() {
		window.location.href="guestList";
	}
</script>
</body>
</html>
