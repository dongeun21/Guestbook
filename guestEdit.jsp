<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <title>방명록 수정</title>
</head>
<body>
    <div class="container w-75 mt-5 mx-auto">
        <h2>방명록 수정</h2>
        <hr>
        
        <form method="post" action="guest?action=updateGuest">
            <input type="hidden" name="idx" value="${guest.idx}">
            <div class="mb-3">
                <label for="writer" class="form-label">작성자</label>
                <input type="text" class="form-control" id="writer" name="writer" value="${guest.writer}">
            </div>
            <div class="mb-3">
	            <label for="email" class="form-label">이메일</label>
	            <input type="text" class="form-control" id="email" name="email" value="${guest.email}">
	        </div>
            <div class="mb-3">
                <label for="title" class="form-label">제목</label>
                <input type="text" class="form-control" id="title" name="title" value="${guest.title}">
            </div>
            <div class="mb-3">
	            <label for="password" class="form-label">비밀번호</label>
	            <input type="password" class="form-control" id="password" name="password" required>
	        </div>
            <div class="mb-3">
                <label for="content" class="form-label">내용</label>
                <textarea class="form-control" id="content" name="content" rows="5">${guest.content}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">수정</button>
            <a href="guest?action=delGuest&idx=${guest.idx}" class="btn btn-danger">삭제</a>
            <a href="guest?action=listGuest" class="btn btn-secondary">목록</a>
        </form>
    </div>
</body>
</html>
