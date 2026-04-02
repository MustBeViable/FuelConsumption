CREATE DATABASE IF NOT EXISTS fuel_calculator_localization
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fuel_calculator_localization;

CREATE TABLE IF NOT EXISTS calculation_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    distance DOUBLE NOT NULL,
    consumption DOUBLE NOT NULL,
    price DOUBLE NOT NULL,
    total_fuel DOUBLE NOT NULL,
    total_cost DOUBLE NOT NULL,
    language VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS localization_strings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `key` VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    language VARCHAR(10) NOT NULL,
    UNIQUE KEY unique_key_lang (`key`, `language`)
);

INSERT INTO localization_strings (`key`, value, language) VALUES
('app.title', 'Fuel Consumption and Trip Cost Calculator', 'en_US'),
('distance.label', 'Distance (km)', 'en_US'),
('consumption.label', 'Fuel Consumption (L/100 km)', 'en_US'),
('price.label', 'Fuel Price (per liter)', 'en_US'),
('calculate.button', 'Calculate Trip Cost', 'en_US'),
('result.label', 'Total fuel needed: {0} L | Total cost: {1}', 'en_US'),
('invalid.input', 'Invalid input, please enter valid numbers.', 'en_US'),
('save.success', 'Saved to database.', 'en_US'),
('save.error', 'Database save failed.', 'en_US'),

('app.title', 'Calculateur de consommation et de coût du trajet', 'fr_FR'),
('distance.label', 'Distance (km)', 'fr_FR'),
('consumption.label', 'Consommation de carburant (L/100 km)', 'fr_FR'),
('price.label', 'Prix du carburant (par litre)', 'fr_FR'),
('calculate.button', 'Calculer le coût du trajet', 'fr_FR'),
('result.label', 'Carburant nécessaire : {0} L | Coût total : {1}', 'fr_FR'),
('invalid.input', 'Entrée invalide, veuillez saisir des nombres valides.', 'fr_FR'),
('save.success', 'Enregistré dans la base de données.', 'fr_FR'),
('save.error', 'Échec de l''enregistrement dans la base.', 'fr_FR'),

('app.title', '燃料消費量と走行費用計算機', 'ja_JP'),
('distance.label', '距離 (km)', 'ja_JP'),
('consumption.label', '燃料消費量 (L/100 km)', 'ja_JP'),
('price.label', '燃料価格 (1リットルあたり)', 'ja_JP'),
('calculate.button', '走行費用を計算', 'ja_JP'),
('result.label', '必要な燃料: {0} L | 合計費用: {1}', 'ja_JP'),
('invalid.input', '無効な入力です。有効な数値を入力してください。', 'ja_JP'),
('save.success', 'データベースに保存しました。', 'ja_JP'),
('save.error', 'データベースへの保存に失敗しました。', 'ja_JP'),

('app.title', 'محاسبه‌گر مصرف سوخت و هزینه سفر', 'fa_IR'),
('distance.label', 'مسافت (کیلومتر)', 'fa_IR'),
('consumption.label', 'مصرف سوخت (لیتر در ۱۰۰ کیلومتر)', 'fa_IR'),
('price.label', 'قیمت سوخت (به ازای هر لیتر)', 'fa_IR'),
('calculate.button', 'محاسبه هزینه سفر', 'fa_IR'),
('result.label', 'سوخت موردنیاز: {0} لیتر | هزینه کل: {1}', 'fa_IR'),
('invalid.input', 'ورودی نامعتبر است، لطفاً اعداد معتبر وارد کنید.', 'fa_IR'),
('save.success', 'در پایگاه داده ذخیره شد.', 'fa_IR'),
('save.error', 'ذخیره‌سازی در پایگاه داده ناموفق بود.', 'fa_IR');
