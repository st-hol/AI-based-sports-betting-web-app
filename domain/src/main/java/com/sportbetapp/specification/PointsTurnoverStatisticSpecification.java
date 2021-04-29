package com.sportbetapp.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sportbetapp.domain.betting.PointsTurnoverStatistic;
import com.sportbetapp.domain.betting.Wager;


public class PointsTurnoverStatisticSpecification implements Specification<PointsTurnoverStatistic> {

    private SearchCriteria criteria;

    public PointsTurnoverStatisticSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<PointsTurnoverStatistic> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(":")) {
            Join<PointsTurnoverStatistic, Wager> userJoin = root.join("wager").join("user");
            return getPredicate(userJoin, builder);
        }
        return null;
    }

    private Predicate getPredicate( Join<PointsTurnoverStatistic, Wager> userJoin, CriteriaBuilder builder) {
        final Path<String> expression = userJoin.get(criteria.getKey());

        if (expression.getJavaType() == String.class) {
            return builder.like(expression, "%" + criteria.getValue() + "%");
        } else {
            return builder.equal(expression, criteria.getValue());
        }
    }
}

//if ("name".equals(criteria.getKey())) {
//        return getPredicate(userJoin, builder);
////            return builder.equal(builder.lower(userJoin.get("name")), criteria.getValue());
//        } else if ("email".equals(criteria.getKey())) {
//        return getPredicate(userJoin, builder);
////			return builder.equal(builder.lower(userJoin.get("email")), criteria.getValue());
//        }
