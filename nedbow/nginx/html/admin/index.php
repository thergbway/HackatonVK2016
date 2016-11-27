<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin page</title>
</head>
<body>
    Настройки<br>
    <form action="/api/settings" method="POST">
        <input type="hidden" name="group_id" value="124202730">
        Название организации(обязательно): <input type="text" name="group_name"><br>
        Краткое описание купона(необязательно): <input type="text" name="coupon_title"><br>
        Подробное описание купона(необязательно): <input type="text" name="coupon_content"><br>
        <input type="submit" value="Сохранить">
    </form>

    <br><br>
    Погашение купона<br>
    <form action="">
        Код: <input type="text" name="coupon_code"><br>
        <input type="submit" value="Погасить купон">
    </form>

    <br><br>
    <a href="https://yandex.ru">Посмотреть от лица клиента</a>
</body>
</html>
