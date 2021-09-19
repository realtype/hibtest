package ru.lokoproject.summer.common.data.query.jpa.hibernate;

import lombok.Setter;
import org.hibernate.Session;
import ru.lokoproject.summer.common.data.query.*;
import ru.lokoproject.summer.common.data.util.FieldPathUtil;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.lokoproject.summer.common.data.query.QueryTypes.*;
import static ru.lokoproject.summer.common.data.util.ConversionUtil.getNumberValue;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HibernateQueryProcessor implements QueryProcessor {

    @Setter
    Session session;

    PathResolver pathResolver = new PathResolver();

    public void processQuery(Query query) {
        Class entityClass = null;
        try {
            entityClass = Class.forName(query.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = builder.createQuery();
        Root root = criteriaQuery.from(entityClass);
        CriteriaQuery where = criteriaQuery.select(root).where(createPredicates(
                Arrays.asList(query), builder, root, null));

        List resultList = session.createQuery(where).getResultList();
        System.out.println("e");

    }

    Predicate[] createPredicates(List<Query> queryList, CriteriaBuilder builder, Root root, Map<String, From> joinMap) {
        Predicate[] result = new Predicate[queryList.size()];
        if (joinMap == null) joinMap = new HashMap<>();

        int i = 0;
        for (Query query : queryList) {

            if (query.isGroup()) {
                GroupQuery groupQuery = (GroupQuery) query;
                if (OR.equalsIgnoreCase(query.getType())) {
                    result[i] = builder.or(createPredicates(groupQuery.getChildQueries(), builder, root, joinMap));
                } else if (AND.equalsIgnoreCase(query.getType())) {
                    result[i] = builder.and(createPredicates(groupQuery.getChildQueries(), builder, root, joinMap));
                } else
                    throw new QueryExecutionException(String.format("unknown group query type '%s'", query.getType()), query);
            } else {

                result[i] = createSpecificPredicate(query, builder, root, joinMap);
            }
            i++;
        }
        return result;
    }

    Predicate createSpecificPredicate(Query query, CriteriaBuilder builder, Root root, Map<String, From> joinMap) {
        SpecificQuery specificQuery = (SpecificQuery) query;
        From from = pathResolver.resolvePath(specificQuery.getPath(), root, joinMap, true);
        String path = FieldPathUtil.getLastPartOfPath(specificQuery.getPath());

        if ((specificQuery.getParams() == null) || (specificQuery.getParams().size() == 0))
            throw new QueryExecutionException("empty param for query", query);

        if (GT.equalsIgnoreCase(specificQuery.getType())) {
            return builder.gt(from.get(path), getNumberValue(specificQuery.getParams().get(0)));
        } else if (GE.equalsIgnoreCase(specificQuery.getType())) {
            return builder.ge(from.get(path), getNumberValue(specificQuery.getParams().get(0)));
        } else if (LT.equalsIgnoreCase(specificQuery.getType())) {
            return builder.lt(from.get(path), getNumberValue(specificQuery.getParams().get(0)));
        } else if (LE.equalsIgnoreCase(specificQuery.getType())) {
            return builder.le(from.get(path), getNumberValue(specificQuery.getParams().get(0)));
        } else if (EQ.equalsIgnoreCase(specificQuery.getType())) {
            return builder.equal(from.get(path), getNumberValue(specificQuery.getParams().get(0)));  // TODO: 26.08.2021 преобразование к типу модели
        } else if (LIKE.equalsIgnoreCase(specificQuery.getType())) {
            return builder.like(from.get(path), specificQuery.getParams().get(0));
        }
        // TODO: 27.08.2021
//            else if(BETWEEN.equalsIgnoreCase(specificQuery.getType())){
//                if(specificQuery.getParams().size() < 2)
//                    throw new  QueryExecutionException("empty second param for between query", query);
//
//                builder.between(root.get(specificQuery.getPath()),
//                        getNumberValue(specificQuery.getParams().get(0)),
//                        getNumberValue(specificQuery.getParams().get(1))
//                        );
//            }


        throw new QueryExecutionException(String.format("unknown specific query type '%s'", query.getType()), query);
    }
}
