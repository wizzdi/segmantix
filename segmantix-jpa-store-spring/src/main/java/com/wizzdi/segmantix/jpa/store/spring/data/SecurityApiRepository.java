package com.wizzdi.segmantix.jpa.store.spring.data;

import com.flexicore.annotations.IOperation;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SecurityApiRepository {

	@PersistenceContext
	private EntityManager em;

	/**
	 * @param operation operation
	 * @param user user
	 * @param access access
	 * @return if the user is allowed/denied (based on given access object) to the given operation
	 */
	public boolean checkRole(Operation operation, User user, IOperation.Access access) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> users = q.from(User.class);
		Join<User, RoleToUser> roleToUser = users.join(User_.roles, JoinType.LEFT);
		Join<RoleToUser, Role> roles =roleToUser.join(RoleToUser_.role, JoinType.LEFT);
		Join<Role, RoleSecurity> roleSecurity = roles.join(Role_.roleSecurity);
		Predicate rolesPredicate = cb.and(
				cb.isFalse(roleToUser.get(RoleToUser_.softDelete)),
				cb.isFalse(roleSecurity.get(RoleSecurity_.softDelete)),
				cb.equal(users.get(User_.id), user.getId()),
				cb.equal(roleSecurity.get(RoleSecurity_.baseclass), operation.getSecurity()),
				cb.equal(roleSecurity.get(RoleSecurity_.access), access)
		);
		List<Predicate> preds = new ArrayList<>();
		preds.add(rolesPredicate);
		q.select(users).where(preds.toArray(Predicate[]::new));
		TypedQuery<User> query = em.createQuery(q);
		List<User> usersList = query.getResultList();
		return !usersList.isEmpty();

	}

	public boolean checkUser(Operation operation, User user, IOperation.Access access) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> users = q.from(User.class);
		// check if this user has direct connection with the operation and the
		// value is Deny.
		Join<User, UserSecurity> direct = users.join(User_.userSecurityes, JoinType.LEFT);
		Predicate directPredicate = cb.and(
				cb.isFalse(direct.get(UserSecurity_.softDelete)),
				cb.equal(users.get(User_.id), user.getId()),
				cb.equal(direct.get(UserSecurity_.baseclass), operation.getSecurity()),
				cb.equal(direct.get(UserSecurity_.access), access));

		List<Predicate> preds = new ArrayList<>();
		preds.add(directPredicate);
		q.select(users).where(preds.toArray(Predicate[]::new));
		TypedQuery<User> query = em.createQuery(q);
		List<User> usersList = query.getResultList();
		return !usersList.isEmpty();
	}
}
