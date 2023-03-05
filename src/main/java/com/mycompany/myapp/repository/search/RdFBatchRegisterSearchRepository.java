package com.mycompany.myapp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.mycompany.myapp.domain.RdFBatchRegister;
import com.mycompany.myapp.repository.RdFBatchRegisterRepository;
import java.util.List;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link RdFBatchRegister} entity.
 */
public interface RdFBatchRegisterSearchRepository
    extends ReactiveElasticsearchRepository<RdFBatchRegister, Long>, RdFBatchRegisterSearchRepositoryInternal {}

interface RdFBatchRegisterSearchRepositoryInternal {
    Flux<RdFBatchRegister> search(String query, Pageable pageable);

    Flux<RdFBatchRegister> search(Query query);
}

class RdFBatchRegisterSearchRepositoryInternalImpl implements RdFBatchRegisterSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    RdFBatchRegisterSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<RdFBatchRegister> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        return search(nativeSearchQuery);
    }

    @Override
    public Flux<RdFBatchRegister> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, RdFBatchRegister.class).map(SearchHit::getContent);
    }
}
