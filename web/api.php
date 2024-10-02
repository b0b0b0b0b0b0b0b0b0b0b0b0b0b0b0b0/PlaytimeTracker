<?php
$dataFile = 'data.json';

$validApiToken = '123123'; // Задайте реальный API-ключ здесь

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $headers = array_change_key_case(getallheaders(), CASE_LOWER);
    if (!isset($headers['authorization']) || $headers['authorization'] !== 'Bearer ' . $validApiToken) {
        http_response_code(403);
        echo json_encode(['error' => 'Access denied. Invalid API token.']);
        exit;
    }

    $data = json_decode(file_get_contents('php://input'), true);

    if ($data && isset($data['player']) && isset($data['playtime'])) {
        $existingData = [];
        if (file_exists($dataFile)) {
            $existingData = json_decode(file_get_contents($dataFile), true);
        }

        $existingData[$data['player']] = [
            'player' => $data['player'],
            'playtime' => $data['playtime']
        ];

        file_put_contents($dataFile, json_encode($existingData, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
        echo "Data received and saved!";
    } else {
        echo "Invalid data format!";
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    header('Content-Type: application/json; charset=utf-8');

    if (isset($_GET['getData'])) {
        if (file_exists($dataFile)) {
            $jsonData = json_decode(file_get_contents($dataFile), true);
            if ($jsonData === null) {
                echo json_encode(['error' => 'Ошибка при чтении data.json']);
                exit;
            }

            echo json_encode($jsonData, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
        } else {
            echo json_encode(['error' => 'Файл data.json не найден']);
        }
    } elseif (isset($_GET['player'])) {
        $playerName = strtolower($_GET['player']);
        if (file_exists($dataFile)) {
            $jsonData = json_decode(file_get_contents($dataFile), true);
            $results = [];

            foreach ($jsonData as $player => $info) {
                if (strpos(strtolower($player), $playerName) !== false) {
                    $results[$player] = $info;
                }
            }

            echo !empty($results) ? json_encode($results, JSON_UNESCAPED_UNICODE) : json_encode(['error' => 'Player not found']);
        } else {
            echo json_encode(['error' => 'No data found']);
        }
    } else {
        echo json_encode(['error' => 'Некорректный запрос']);
    }
}
?>
