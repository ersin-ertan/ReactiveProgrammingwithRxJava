package com.nullcognition.reactiveprogrammingwithrxjava.chap02;

import com.nullcognition.reactiveprogrammingwithrxjava.MainActivity;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

/**
 * Created by mms on 11/21/16.
 */

public class P04 {

  public P04() {
    Observable.timer(100, TimeUnit.MILLISECONDS);

    //withoutConnect();
    //withConnect();
    //withoutRefCount();
    withRefCount();
  }

  /*
  * However this showcases the wait that occurs when subscribing, until connect is called
  * */
  private void withConnect() {
    ConnectableObservable<Long> connectableInterval =
        Observable.interval(100, TimeUnit.MILLISECONDS).take(100).publish();

    Subscription s1 = connectableInterval.subscribe(MainActivity::p);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Subscription s2 = connectableInterval.subscribe(MainActivity::p);
    connectableInterval.connect();
  }


  /*
* Because interval is cold by nature, not hot to which every subscribes to the same interval
* showcasing the missed values is not possible.
* */

  private void withoutConnect() {
    Observable<Long> interval = Observable.interval(100, TimeUnit.MILLISECONDS).take(100);
    Subscription s1 = interval.subscribe(MainActivity::p);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Subscription s2 = interval.subscribe(MainActivity::p);
  }

  private void withoutRefCount() {
    Observable<Integer> rc = Observable.create(subscriber -> {
      MainActivity.p("Takes along time to create");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      subscriber.onNext(1);
    });

    Subscription s1 = rc.subscribe(MainActivity::p);
    Subscription s2 = rc.subscribe(MainActivity::p);
  }

  private void withRefCount() {
    Observable<Object> rc = Observable.create(subscriber -> {
      MainActivity.p("Takes along time to create");
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      subscriber.onNext(1);
    }).publish();

    Subscription s1 = rc.subscribe(MainActivity::p);
    Subscription s2 = rc.subscribe(MainActivity::p);
    // TODO: 11/21/16 Why isn't the second on Next showing?

    rc.publish();
  }
}
