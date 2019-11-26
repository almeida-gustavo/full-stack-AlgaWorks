package com.exemplo.algamoney.api.repository.implementacaoLancamento;


import com.exemplo.algamoney.api.dto.LancamentoEstatisticaCategoria;
import com.exemplo.algamoney.api.model.Lancamento;
import com.exemplo.algamoney.api.model.Lancamento_;
import com.exemplo.algamoney.api.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;


    @Override
    public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.
                createQuery(LancamentoEstatisticaCategoria.class);

        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class,
                root.get(Lancamento_.categoria),
                criteriaBuilder.sum(root.get(Lancamento_.valor))));

        LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
        LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());

        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
                        primeiroDia),
                criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
                        ultimoDia));

        criteriaQuery.groupBy(root.get(Lancamento_.categoria));

        TypedQuery<LancamentoEstatisticaCategoria> typedQuery = manager
                .createQuery(criteriaQuery);

        return typedQuery.getResultList();

    }

    @Override
    public Page<Lancamento> filtrate(LancamentoFilter lancamentoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
//      Para começar a definir as restrições, vc vai utilizar o root
        Root<Lancamento> root = criteria.from(Lancamento.class);

        //Criar restrições
        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        //CRIAR ORDENAÇÃO
        //criteria.orderBy(builder.asc(root.get(Lancamento_.descricao)));


        TypedQuery<Lancamento> query = manager.createQuery(criteria);
        //COLOCAR A QUANTIDADE DE PAGINAÇÕES QUE TERA
        adicionarRestricoesDePaginacao(query, pageable);


        return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
    }


    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root){

        List<Predicate> predicates = new ArrayList<>();


        if(!StringUtils.isEmpty(lancamentoFilter.getDescricao())){
            predicates.add(builder.like(
                    builder.lower(root.get(Lancamento_.DESCRICAO)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
        }

        if(lancamentoFilter.getDataVencimentoDe() != null){
            predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.DATA_VENCIMENTO), lancamentoFilter.getDataVencimentoDe()));
        }

        if(lancamentoFilter.getDataVencimentoAte() != null){
            predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> query, Pageable pageable) {
        //pageNumber eh a informação que vai vir la da uri  quando vc coloca page= alguma coisa, COMEÇA SEMPRE NA PAGINA 0
        int paginaAtual = pageable.getPageNumber();
        //Informação que vem la da uri também no campo size
        int totalRegistroPagina = pageable.getPageSize();
        //eh aqui que ele vai definir a partir de qual vai começar a mostrar para o usuário;
        //se vc colocar page 1 e size 3 ele vai mostrar a partir do registro 3.... (segunda pagina)
        int primeiroRegistroPagina = paginaAtual * totalRegistroPagina;

        //SETANDO A PAGINAÇÃO DA SUA PESQUISA DE ACORDO COM O QUE VC DEFINIU COMO PAGE E SIZE
        query.setFirstResult(primeiroRegistroPagina);
        query.setMaxResults(totalRegistroPagina);

    }

    //O TOTAL DE LANÇAMENTOS DISPONIVELS
    private Long total(LancamentoFilter lancamentoFilter) {
        //vc vai precisar criar uma nova query

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Lancamento> root = criteria.from(Lancamento.class);

        Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
        criteria.where(predicates);

        //CONTA QUANTOS REGISTROS TEM
        criteria.select(builder.count(root));

        return manager.createQuery(criteria).getSingleResult();

    }
}
