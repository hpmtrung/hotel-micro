package com.hba.common.messaging;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CommandMessage<T extends Command> {

  @EqualsAndHashCode.Include private String id;
  private T command;
  private String outChannel;
  private String producerInChannel;

  public static <T extends Command> CommandMessage<T> clone(CommandMessage<T> cm) {
    return new CommandMessage<>(cm.id, cm.command, cm.outChannel, cm.producerInChannel);
  }

  public static <T extends Command> CommandMessage<T> of(T command) {
    return new CommandMessage<>(null, command, null, null);
  }

  public CommandMessage<T> to(String outChannel) {
    this.outChannel = outChannel;
    return this;
  }
}