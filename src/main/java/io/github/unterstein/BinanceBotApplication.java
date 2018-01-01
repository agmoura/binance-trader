package io.github.unterstein;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@SpringBootApplication
@RestController("/")
public class BinanceBotApplication {

    private static Logger logger = LoggerFactory.getLogger(BinanceBotApplication.class);

    private List<BinanceTrader> traders = new ArrayList<>();

    @Value("${TRADE_DIFFERENCE:0.00000001}")
    private double tradeDifference;

    @Value("${TRADE_PROFIT:1.3}")
    private double tradeProfit;

    @Value("${TRADE_AMOUNT:94}")
    private int tradeAmount;

    @Value("${BASE_CURRENCY:BTC}")
    private String baseCurrency;

    @Value("${TRADE_CURRENCY:XVG}")
    private String tradeCurrency;

    @Value("${API_KEY}")
    private String apiKey;

    @Value("${API_SECRET}")
    private String apiSecret;

    @PostConstruct
    public void init() {

        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, apiSecret);
        BinanceApiRestClient client = factory.newRestClient();

        logger.info(String.format("Starting app with diff=%.8f, profit=%.8f amount=%d base=%s trade=%s", tradeDifference, tradeProfit, tradeAmount, baseCurrency, tradeCurrency));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 94, baseCurrency, "XVG"));
        //traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 21, baseCurrency, "ADA"));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 2, baseCurrency, "BNB"));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 8, baseCurrency, "XRP"));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 326, baseCurrency, "TRX"));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 32, baseCurrency, "XLM"));
        traders.add(new BinanceTrader(client, tradeDifference, tradeProfit, 0.021, baseCurrency, "ETH"));
    }

    // tick every 3 seconds
    @Scheduled(fixedRate = 3000)
    public void schedule() {
        for (BinanceTrader trader : traders) {
            trader.tick();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BinanceBotApplication.class);
    }
}
