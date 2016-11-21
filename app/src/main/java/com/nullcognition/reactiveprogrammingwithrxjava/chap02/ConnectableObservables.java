package com.nullcognition.reactiveprogrammingwithrxjava.chap02;

import com.nullcognition.reactiveprogrammingwithrxjava.MainActivity;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.observables.ConnectableObservable;

/**
 * Created by mms on 11/20/16.
 * Subjects are imperative ways of createing observables
 * ConOBs shields the original upstearm observable and guarantees at most one subscriber
 * reaches it, Thus it opens up only one subscription ot the observable
 * from which it was created no matter the number of subscribers[
 *
 * TODO: requires more study
 */

public class ConnectableObservables {

  List<Integer> naiveList = new ArrayList<>(6);

  public ConnectableObservables() {
    MainActivity.src();
    //ConnectableObservable<Integer> connectableObservable = ConnectableObservable.create();
  }

  void lifecycle() {

    /*
    * publish() may force the subscription in the absence of a subscriber
    * */
  }

  Observable<Integer> naive() {
    Observable<Integer> ints = Observable.from(new Integer[] { 1, 2, 3, 4, 5, 6 });
    return ints.doOnNext(integer -> naiveList.add(integer));
    // because observables are lazy, doOnNext is not triggered until one is subscribed to.

    // we need some sort of fake observer that does not use the values but subscribes to the stream
    // to force the production of events, thus you can use the empty subscriber which is overloaded
  }

  void overloadedSubscribe() {
    naive().subscribe(); // provides
    //Action1<T> onNext = Actions.empty();
    //Action1<Throwable> onError = InternalObservableUtils.ERROR_NOT_IMPLEMENTED;
    //Action0 onCompleted = Actions.empty();
    //return subscribe(new ActionSubscriber<T>(onNext, onError, onCompleted));

    // however if we require a new expensive connection, each other subscriber will establish the
    // connection itself, thus we use publish.connect(); which creates the artificial sub
    // immediately, keeping only one upstream subscriber.
    //
    ConnectableObservable<Integer> published = MainActivity.src().publish();
    published.connect();

    /** calling publish on any observvable return the connectable observable, we can use the src()
     observable because publish does not affect it. All other subs to published(the connected obs)
     is in the set of subscribers, and until connect is called, the subscribers are paused and not
     connected to the upstream observable.

     This is useful in the event where multiple subscribers are interested in an observable source
     but subscribe at different times. The first subscription may trigger a hot observable thus,
     subscribers comming in at a later time will not have the same world view as each other.

     Thus when you reach a point of which you know that all of the consumers are subscribed and are
     ready to start producing, then you connect and unleash the hounds. We do not want to start 2ice
     or initiate additional connections, or have inconsistent state.

     **/

  }
}
