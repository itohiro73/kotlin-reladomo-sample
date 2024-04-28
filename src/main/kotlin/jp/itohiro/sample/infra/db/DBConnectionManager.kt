package jp.itohiro.sample.infra.db

import com.gs.fw.common.mithra.bulkloader.BulkLoader
import com.gs.fw.common.mithra.bulkloader.BulkLoaderException
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager
import com.gs.fw.common.mithra.databasetype.DatabaseType
import java.sql.Connection
import java.util.*

class DBConnectionManager : SourcelessConnectionManager {
    private var xaConnectionManager: XAConnectionManager? = null

    private var jdbcDriverClassName: String? = null
    private var jdbcSubProtocol: String? = null
    private var host: String? = null
    private var port: String? = null
    private var database: String? = null
    private var username: String? = null
    private var password: String? = null
    private var timeZone: TimeZone? = null

    /**
     * Set the properties necessary to create a connection to database.
     *
     * @param properties Properties defined in MithraRuntime XML file.
     * @apiNote This method is called after instance initialization.
     */
    private fun init(properties: Properties) {
        this.jdbcDriverClassName = properties.getProperty(JDBC_DRIVER_CLASS_NAME_KEY)
        this.jdbcSubProtocol = properties.getProperty(JDBC_SUB_PROTOCOL_KEY)
        this.host = properties.getProperty(HOST_KEY)
        this.port = properties.getProperty(PORT_KEY)
        this.database = properties.getProperty(DATABASE_KEY)
        this.username = properties.getProperty(USERNAME_KEY)
        this.password = properties.getProperty(PASSWORD_KEY)
        this.timeZone = TimeZone.getTimeZone(properties.getProperty(TIMEZONE_KEY))
        this.createConnectionManager()
    }

    /**
     * Set the value to XAConnectionManager.
     *
     * @apiNote XAConnectionManager is a utility class for a transactional connection manager.
     */
    private fun createConnectionManager() {
        this.xaConnectionManager = XAConnectionManager()
        xaConnectionManager!!.setDriverClassName(jdbcDriverClassName)
        xaConnectionManager!!.jdbcConnectionString = "jdbc:$jdbcSubProtocol://$host:$port/$database"
        xaConnectionManager!!.jdbcUser = username
        xaConnectionManager!!.jdbcPassword = password
        xaConnectionManager!!.poolName = "$host:$database: connection pool"
        xaConnectionManager!!.initialSize = 1
        xaConnectionManager!!.poolSize = 10
        xaConnectionManager!!.initialisePool()
    }

    /**
     * Returns a connection from the pool.
     *
     * @return connection
     * @apiNote If all connections are in use, this method will block, unless maxWait has been set.
     */
    override fun getConnection(): Connection {
        return xaConnectionManager!!.connection
    }

    /**
     * Returns the database transactionType.
     *
     * @apiNote XAConnectionManager is a utility class for a transactional connection manager.
     */
    override fun getDatabaseType(): DatabaseType {
        return xaConnectionManager!!.databaseType
    }

    /**
     * Returns the timezone the database server is located in.
     *
     * @return timezone
     */
    override fun getDatabaseTimeZone(): TimeZone {
        return timeZone!!
    }

    /**
     * Returns the database host name and port number.
     *
     * @apiNote XAConnectionManager is a utility class for a transactional connection manager.
     */
    override fun getDatabaseIdentifier(): String {
        return xaConnectionManager!!.hostName + ":" + xaConnectionManager!!.port
    }

    @Throws(BulkLoaderException::class)
    override fun createBulkLoader(): BulkLoader? {
        return null
    }

    companion object {
        protected var instance: DBConnectionManager? = null
        private const val JDBC_DRIVER_CLASS_NAME_KEY = "jdbcDriverClassName"
        private const val JDBC_SUB_PROTOCOL_KEY = "jdbcSubProtocol"
        private const val HOST_KEY = "host"
        private const val PORT_KEY = "port"
        private const val DATABASE_KEY = "database"
        private const val USERNAME_KEY = "username"
        private const val PASSWORD_KEY = "password"
        private const val TIMEZONE_KEY = "timeZone"

        @JvmStatic
        @Synchronized
        fun getInstance(properties: Properties): DBConnectionManager? {
            if (instance == null) {
                instance = DBConnectionManager()
            }
            instance!!.init(properties)
            return instance
        }
    }
}