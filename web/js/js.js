function formatPlaytime(seconds) {
    const units = [
        { singular: 'год', genitive_singular: 'года', genitive_plural: 'лет', value: 31536000 },
        { singular: 'месяц', genitive_singular: 'месяца', genitive_plural: 'месяцев', value: 2592000 },
        { singular: 'неделя', genitive_singular: 'недели', genitive_plural: 'недель', value: 604800 },
        { singular: 'день', genitive_singular: 'дня', genitive_plural: 'дней', value: 86400 },
        { singular: 'час', genitive_singular: 'часа', genitive_plural: 'часов', value: 3600 },
        { singular: 'минута', genitive_singular: 'минуты', genitive_plural: 'минут', value: 60 },
        { singular: 'секунда', genitive_singular: 'секунды', genitive_plural: 'секунд', value: 1 }
    ];

    let time = [];

    for (let i = 0; i < units.length; i++) {
        const unitValue = units[i].value;
        if (seconds >= unitValue) {
            const amount = Math.floor(seconds / unitValue);
            seconds %= unitValue;
            const unitForm = getDeclension(amount, units[i]);
            time.push(amount + ' ' + unitForm);
        }
    }

    return time.length > 0 ? time.join(', ') : '0 минут';
}

function getDeclension(number, forms) {
    number = Math.abs(number) % 100;
    let n1 = number % 10;

    if (number > 10 && number < 20) {
        return forms.genitive_plural;
    }
    if (n1 > 1 && n1 < 5) {
        return forms.genitive_singular;
    }
    if (n1 == 1) {
        return forms.singular;
    }
    return forms.genitive_plural;
}


function updateAllPlayers() {
    fetch('api.php?getData')
        .then(response => response.json())
        .then(data => {
            const playerList = document.getElementById('playerList');
            playerList.innerHTML = '';

            for (const player in data) {
                const playerCard = document.createElement('div');
                playerCard.classList.add('player-card');

                const avatar = document.createElement('img');
                avatar.src = `https://minotar.net/avatar/${player}/50`;
                avatar.alt = `${player} avatar`;
                avatar.classList.add('player-avatar');

                const playerInfo = document.createElement('div');
                playerInfo.classList.add('player-info');

                const playerName = document.createElement('p');
                playerName.classList.add('player-name');
                playerName.textContent = player;

                const playerStats = document.createElement('p');
                playerStats.classList.add('player-stats');
                
                const formattedPlaytime = formatPlaytime(data[player].playtime);
                playerStats.textContent = `Игровое время: ${formattedPlaytime}`;

                playerInfo.appendChild(playerName);
                playerInfo.appendChild(playerStats);

                playerCard.appendChild(avatar);
                playerCard.appendChild(playerInfo);

                playerList.appendChild(playerCard);
            }
        })
        .catch(error => console.error('Ошибка при получении данных:', error));
		
		
}
