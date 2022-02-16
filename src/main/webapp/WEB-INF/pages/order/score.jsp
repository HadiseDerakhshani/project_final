<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>score Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="<c:url value="/static/css/main.css"/>">

</head>
<body>
<div class="container">
    <h1>${message}</h1>
    <h2>${customer.firstName} ${customer.lastName} </h2>
    <h3>${order.service.name} ,${order.status}</h3>
    <p> choose select option of payment</p>
    <h3>${amount}</h3>
    <div>

        <form action="/expert/registerScore" method="post">
            Score to expert :
            <br>
            <input type="text" placeholder="enter a number between 1-10" name="score" id="score" class="form-control" require>

            <br><br>
            Comment :
            <br>
            <input type="text" placeholder="enter a number between 1-10" name="comment" class="form-control">
            <button type="submit" class="btn btn-primary" style="border-bottom: darkblue" >register</button>
        </form>

    </div>

</div>
<script>
    $("form").submit(function (event) {

        if ( $("#score").val() >= 1 && $("#score").val() <= 10) {
                return;
        }
        $("span").text("score is empty!").show().fadeOut(2000);
        event.preventDefault();
    });
</script>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
        integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
</body>
</html>