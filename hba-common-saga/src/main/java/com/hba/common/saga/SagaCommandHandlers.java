package com.hba.common.saga;

import com.hba.common.messaging.Command;
import com.hba.common.messaging.CommandMessage;
import com.hba.common.messaging.HandlerNotFoundException;
import com.hba.common.messaging.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SagaCommandHandlers {

  private final String inChannel;
  private final Map<Class<? extends Command>, Function<? extends CommandMessage<?>, Message>>
      commandHandlers;

  private SagaCommandHandlers(Builder builder) {
    this.inChannel = builder.inChannel;
    this.commandHandlers = builder.commandHandlers;
  }

  public static Builder fromChannel(String inChannel) {
    return new Builder(inChannel);
  }

  public String getInChannel() {
    return inChannel;
  }

  public Message handleCommand(CommandMessage<?> command) {
    Class<? extends Command> commandClass = command.getCommand().getClass();
    if (commandHandlers.containsKey(commandClass)) {
      return ((Function<CommandMessage<?>, Message>) commandHandlers.get(commandClass))
          .apply(command);
    }
    throw new HandlerNotFoundException();
  }

  @Override
  public String toString() {
    return "SagaCommandHandlers{"
        + "inChannel='"
        + inChannel
        + '\''
        + ", commandHandlers="
        + commandHandlers
        + '}';
  }

  public static class Builder {
    private final String inChannel;
    private final Map<Class<? extends Command>, Function<? extends CommandMessage<?>, Message>>
        commandHandlers = new HashMap<>();

    public Builder(String inChannel) {
      this.inChannel = inChannel;
    }

    public <T extends Command> Builder onMessage(
        Class<T> commandClass, Function<CommandMessage<T>, Message> handler) {
      commandHandlers.put(commandClass, handler);
      return this;
    }

    public SagaCommandHandlers build() {
      return new SagaCommandHandlers(this);
    }
  }
}