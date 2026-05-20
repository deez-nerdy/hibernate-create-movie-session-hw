package mate.academy.dao.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.CinemaHallDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.CinemaHall;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class CinemaHallDaoImpl implements CinemaHallDao {
    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = openSession();
            transaction = session.beginTransaction();
            session.persist(cinemaHall);
            transaction.commit();
            return cinemaHall;
        } catch (Exception e) {
            transactionNullCheck(transaction);
            throw new DataProcessingException("Could not insert cinema hall: " + cinemaHall, e);
        } finally {
            sessionNullCheck(session);
        }
    }

    @Override
    public Optional<CinemaHall> get(Long id) {
        try (Session session = openSession()) {
            CinemaHall cinemaHall = session.get(CinemaHall.class, id);
            return Optional.ofNullable(cinemaHall);
        } catch (Exception e) {
            throw new DataProcessingException("Could not get cinema hall with id: " + id, e);
        }
    }

    @Override
    public List<CinemaHall> getAll() {
        try (Session session = openSession()) {
            List<CinemaHall> cinemaHallList
                    = session.createQuery("from CinemaHall", CinemaHall.class).getResultList();
            return cinemaHallList;
        } catch (Exception e) {
            throw new DataProcessingException("Could not get cinema halls list from DB", e);
        }
    }

    private Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    private void transactionNullCheck(Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    private void sessionNullCheck(Session session) {
        if (session != null) {
            session.close();
        }
    }
}
