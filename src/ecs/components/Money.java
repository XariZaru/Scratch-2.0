package ecs.components;

import com.artemis.Component;

import java.util.concurrent.atomic.AtomicInteger;

public class Money extends Component {
	public AtomicInteger amount = new AtomicInteger(0);
}
