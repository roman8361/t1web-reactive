package ru.kravchenko.limit.sheduller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.kravchenko.limit.repository.ClientLimitRepository;

import java.math.BigDecimal;

import static ru.kravchenko.common.utill.Constant.DAILY_LIMIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class LimitScheduler {
    private final ClientLimitRepository clientLimitRepository;

    @Scheduled(cron = "${scheduler.cronExpression}")
    public void startUpdateLimit() {
        log.info("Запущен процесс обнуления лимитов, для каждого клиента установлен лимит: {}", DAILY_LIMIT);
        processUpdateLimit()
                .subscribe();
    }

    private Mono<Void> processUpdateLimit() {
        return clientLimitRepository.findAll()
                .flatMap(it -> {
                    it.setCurrentLimit(BigDecimal.valueOf(DAILY_LIMIT));
                    return clientLimitRepository.save(it);
                })
                .then();
    }
}
