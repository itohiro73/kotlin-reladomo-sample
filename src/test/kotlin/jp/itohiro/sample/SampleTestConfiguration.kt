package jp.itohiro.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

/**
 * テストで単体でSpringBoot起動するための設定ファイル
 * このクラスが配置してあるパッケージ以下がDIのスキャン対象になる
 * (これはデフォルト設定のため、変更可能)
 */
@Configuration
@ConfigurationPropertiesScan
@SpringBootApplication
class SampleTestConfiguration
