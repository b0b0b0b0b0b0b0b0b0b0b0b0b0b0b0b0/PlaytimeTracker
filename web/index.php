<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Статистика сервера Minecraft</title>
    <style>
        
    </style>
	<link rel="stylesheet" href="css/css.css">
</head>
<body>
    <div class="container">
        <h1>Статистика сервера Minecraft</h1>

        <input type="text" id="searchPlayer" placeholder="Введите имя игрока" oninput="searchPlayer()">
        <div id="playerInfo"></div>

        <h2>Все игроки</h2>
        <div id="playerList"></div>
    </div>

  <script src="js/js.js"></script>
<script src="js/js1.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        updateAllPlayers();
    });
</script>
</body>
</html>
