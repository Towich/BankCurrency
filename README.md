# BankCurrency
**BankCurrency** - это мобильное приложение, разработанное в рамках тестового задания в ПАО "Промсвязьбанк".

# Описание
Данное приложение позволяет получить список валют с отображением названия и текущего курса валюты по отношению к рублю.

# Скриншоты
<img src="https://github.com/Towich/BankCurrency/assets/100920758/f6025ce8-e88e-4f54-b904-231e20d2b64f" width="250">
<img src="https://github.com/Towich/BankCurrency/assets/100920758/cd84e58b-d949-4174-8d40-3089c1c44a6c" width="250">
<img src="https://github.com/Towich/BankCurrency/assets/100920758/26dc7e9d-e00a-437a-b6c0-72161092571d" width="250">

# Функционал
* Просмотр списка валют
* Просмотр информации о последних обновлениях списка (на устройстве и сервере)
* Автоматическое обновление списка валют раз в 30 секунд
* Возможность обновления списка валют по свайпу
* Информирование о потере интернет-соединения

# Используемые технологии
* Android SDK
* Kotlin
* XML, View Binding
* MVVM
* LiveData
* Kotlin Coroutines
* Retrofit2, OkHttp3, Gson

# Дополнительная информация 
Источником данных о валютах является ЦБ РФ - https://www.cbr-xml-daily.ru/daily_json.js
