package mate.academy.dao.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.MovieSessionDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.MovieSession;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Dao
public class MovieSessionDaoImpl implements MovieSessionDao {
    @Override
    public MovieSession add(MovieSession movieSession) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = openSession();
            transaction = session.beginTransaction();
            session.persist(movieSession);
            transaction.commit();
            return movieSession;
        } catch (Exception e) {
            transactionNullCheck(transaction);
            throw new DataProcessingException("Could not insert movie session: " + movieSession,e);
        } finally {
            sessionNullCheck(session);
        }
    }

    @Override
    public Optional<MovieSession> get(Long id) {
        try (Session session = openSession()) {
            MovieSession movieSession = session.get(MovieSession.class, id);
            return Optional.ofNullable(movieSession);
        } catch (Exception e) {
            throw new DataProcessingException("Could not get movie session with id: " + id, e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSession(Long movieId, LocalDate date) {
        try (Session session = openSession()) {
            List<MovieSession> movieSessionList = session.createQuery("from MovieSession m "
                    + "where m.showTime = :date", MovieSession.class)
                    .setParameter("date", date)
                    .getResultList();
            return movieSessionList;
        } catch (Exception e) {
            throw new RuntimeException("Could not get available session list from DB", e);
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
