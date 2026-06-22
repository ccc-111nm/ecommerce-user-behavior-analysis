import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

// 创建SparkSession
val spark = SparkSession.builder()
  .appName("EcommerceAnalysisScala")
  .getOrCreate()

// 定义MySQL连接信息
val jdbcUrl = "jdbc:mysql://192.168.199.128:3306/data"
val connectionProperties = new java.util.Properties()
connectionProperties.put("user", "spark")
connectionProperties.put("password", "123123")
connectionProperties.put("driver", "com.mysql.cj.jdbc.Driver")

// 从MySQL读取数据
val table1DF = spark.read.jdbc(jdbcUrl, "`电商用户行为表`", connectionProperties)
table1DF.createOrReplaceTempView("电商用户行为表")

// ---------------------- 分析1：每个用户的总购买商品数量 ----------------------
val userTotalQuantity = spark.sql("""
    SELECT 
        用户ID, 
        SUM(商品数量) AS 总购买数量 
    FROM 
        电商用户行为表 
    GROUP BY 
        用户ID
    ORDER BY 总购买数量 DESC
""")
userTotalQuantity.show()

// ---------------------- 分析2：各商品类型的总销量 ----------------------
val productTypeTotal = spark.sql("""
    SELECT 
        商品类型, 
        SUM(商品数量) AS 品类总销量 
    FROM 
        电商用户行为表 
    GROUP BY 
        商品类型
""")
productTypeTotal.show()

// ---------------------- 分析3：各商品类型的购买次数 ----------------------
val productTypeCount = spark.sql("""
    SELECT 
        商品类型, 
        COUNT(*) AS 购买次数 
    FROM 
        电商用户行为表 
    GROUP BY 
        商品类型
""")
productTypeCount.show()

// ---------------------- 分析4：各地区各商品类型的销量 ----------------------
val locationTypeTotal = spark.sql("""
    SELECT 
        所在地, 
        商品类型, 
        SUM(商品数量) AS 地区品类销量 
    FROM 
        电商用户行为表 
    GROUP BY 
        所在地, 商品类型
    ORDER BY 所在地, 地区品类销量 DESC
""")
locationTypeTotal.show()

// ---------------------- 分析5：购买数量最多的Top10用户 ----------------------
val top10User = spark.sql("""
    SELECT 
        用户ID, 
        SUM(商品数量) AS 总购买量 
    FROM 
        电商用户行为表 
    GROUP BY 
        用户ID 
    ORDER BY 
        总购买量 DESC 
    LIMIT 10
""")
top10User.show()