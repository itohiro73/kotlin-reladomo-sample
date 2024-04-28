package jp.itohiro.sample.infra.config

import com.gs.fw.common.mithra.MithraManagerProvider
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration

@Configuration
class ReladomoConfig {
    val MAX_TRANSACTION_TIMEOUT: Int = 60 * 1000 // (seconds)

    @PostConstruct
    @Throws(Exception::class)
    fun postConstruct() {
        initializeReladomo()
        loadReladomoXMLFromClasspath("reladomo/config/MithraRuntimeConfiguration.xml")
    }

    /**
     * Initialize Reladomo using MithraManager class.
     */
    fun initializeReladomo() {
        val mithraManager = MithraManagerProvider.getMithraManager()
        mithraManager.transactionTimeout = MAX_TRANSACTION_TIMEOUT
    }

    /**
     * Load Reladomo runtime configuration file.
     *
     * @param fileName MithraRuntime XML file
     * @return stream
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun loadReladomoXMLFromClasspath(fileName: String) {
        val stream = ReladomoConfig::class.java.classLoader.getResourceAsStream(fileName)
            ?: throw Exception("Failed to locate $fileName in classpath")
        MithraManagerProvider.getMithraManager().readConfiguration(stream)
        stream.close()
    }
}