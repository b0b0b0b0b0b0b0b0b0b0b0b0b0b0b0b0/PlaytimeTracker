function searchPlayer() {
    const playerName = document.getElementById('searchPlayer').value;
    if (playerName) {
        fetch('api.php?player=' + playerName)
            .then(response => response.json())
            .then(data => {
                const playerInfo = document.getElementById('playerInfo');
                const playerList = document.getElementById('playerList');
                playerList.innerHTML = '';

                if (data.error) {
                    playerInfo.innerHTML = `<p style="color: red;">${data.error}</p>`;
                } else {
                    playerInfo.innerHTML = '';

                    Object.keys(data).forEach(player => {
                        const playerCard = document.createElement('div');
                        playerCard.classList.add('player-card');

                        const avatar = document.createElement('img');
                        avatar.src = `https://minotar.net/avatar/${player}/50`;
                        avatar.alt = `${player} avatar`;
                        avatar.classList.add('player-avatar');

                        const playerInfoDiv = document.createElement('div');
                        playerInfoDiv.classList.add('player-info');

                        const playerNameElem = document.createElement('p');
                        playerNameElem.classList.add('player-name');
                        playerNameElem.textContent = player;

                        const playerStats = document.createElement('p');
                        playerStats.classList.add('player-stats');
                        
                        const formattedPlaytime = formatPlaytime(data[player].playtime);
                        playerStats.textContent = `Игровое время: ${formattedPlaytime}`;

                        playerInfoDiv.appendChild(playerNameElem);
                        playerInfoDiv.appendChild(playerStats);

                        playerCard.appendChild(avatar);
                        playerCard.appendChild(playerInfoDiv);

                        playerList.appendChild(playerCard);
                    });
                }
            })
            .catch(error => console.error('Ошибка при получении данных:', error));
    } else {
        document.getElementById('playerInfo').innerHTML = '';
        document.getElementById('playerList').innerHTML = '';
        updateAllPlayers();
    }
}
