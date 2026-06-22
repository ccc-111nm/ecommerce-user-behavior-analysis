-- 创建数据库
CREATE DATABASE IF NOT EXISTS 电商用户行为数据库;
USE 电商用户行为数据库;

-- 创建用户行为表
CREATE TABLE IF NOT EXISTS 电商用户行为表 (
    编号 INT AUTO_INCREMENT PRIMARY KEY,
    用户ID INT NOT NULL,
    商品数量 INT NOT NULL,
    商品类型 VARCHAR(50) NOT NULL,
    购买时间 TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    所在地 VARCHAR(100) NOT NULL
);

-- 插入1万条模拟数据的存储过程
DELIMITER //
CREATE PROCEDURE 插入电商用户行为数据()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE 随机用户ID INT;
    DECLARE 随机商品数量 INT;
    DECLARE 随机商品类型 VARCHAR(50);
    DECLARE 随机所在地 VARCHAR(100);
    
    WHILE i <= 10000 DO
        SET 随机用户ID = FLOOR(RAND() * 1000) + 1;
        SET 随机商品数量 = FLOOR(RAND() * 10) + 1;
        SET 随机商品类型 = ELT(FLOOR(RAND() * 3) + 1, '电子产品', '服装', '文学作品');
        SET 随机所在地 = ELT(FLOOR(RAND() * 3) + 1, '北京', '上海', '广州');
        
        INSERT INTO 电商用户行为表 (用户ID, 商品数量, 商品类型, 购买时间, 所在地) 
        VALUES (随机用户ID, 随机商品数量, 随机商品类型, NOW() - INTERVAL FLOOR(RAND() * 365) DAY, 随机所在地);
        
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

-- 执行存储过程生成数据
CALL 插入电商用户行为数据();