#!/bin/perl

=encoding UTF-8

=head1 NAME

manage.pl: Скрипт для управления приложением

=cut

use utf8;
use strict;
use warnings;

use File::Spec;
use Data::Dumper;
use Getopt::Long;
use Carp qw(croak);
use Term::ANSIColor qw(:constants);

use DBI;


binmode(STDOUT, ':utf8');

Getopt::Long::Configure('no_ignore_case');

GetOptions(
    # users
    'create-user=i' => \&create_user,
);

use constant COLORS => {
    red     => RED,
    green   => GREEN,
    blue    => BLUE,
    yellow  => YELLOW,
    magenta => MAGENTA,
    cyan    => CYAN,
    white   => WHITE,
    brown   => BLUE
};

my $dbh = eval {
    DBI->connect(
        "dbi:SQLite:dbname=app.db",
        '',  # no user
        '',  # no passwd
        {RaiseError => 1, PrintError => 1, AutoCommit => 1, sqlite_unicode => 1}
    );
} or croak("DATABASE_CONNECTION_ERROR: $DBI::errstr");

sub create_user {
    my ($opt, $value) = @_;

    my $is_admin = $value && $value == 1;

    _clrprint(undef, "\n>> Creating " . ($is_admin ? 'admin user' : 'regular user') . "...\n\n", 'green');

    my $username = _input('Enter username: ');
    my $email    = _input('Enter email: ');

    croak 'Invalid email: ' . $email unless $email =~ /^.+\@.+\..+$/mx;

    my $passwd = _input('Enter password: ');

    croak 'Password too short: ' . length $passwd . ' (min 6)' if length $passwd < 6;
    croak 'Password too long: ' . length $passwd . ' (max 32)' if length $passwd > 32;

    my $passwd_confirm = _input('Confirm password: ');
    croak 'Error! Password missmatch!' if $passwd ne $passwd_confirm;

    my $sql = <<SQL;
INSERT INTO users (email, username, password, is_admin, is_activated)
     VALUES (?, ?, ?, ?, 1)
SQL

    my $sth = $dbh->prepare($sql);
    my $result = $sth->exec($email, $username, $passwd, $is_admin);

    croak 'Failed user creation' unless $result;

    _clrprint(undef, "\n>> User '$username' created successful\n\n", 'white');

    exit;
}

sub _input {
    my $msg = shift || 'Enter value: ';
    print $msg;
    my $str = <>;
    chomp($str);
    return $str;
}

sub _clrprint {
    my ($event, $msg, $color) = @_;

    return 0 unless $msg;

    my $clr =
        (defined $color && defined COLORS->{$color})
        ? COLORS->{$color}
        : COLORS->{white};

    print '[', COLORS->{yellow}, uc($event), RESET, '] ' if (defined $event);
    print BOLD, $clr, $msg, RESET;

    return 1;
}

1;

__END__

=head1 USAGE

  carton exec script/manage.pl --create-user=i (1 - supersuer, 0 - regular user)

=head1 AUTHOR

Peter Brovchenko <peter.brovchenko@gmail.ru>

=head1 METHODS

=over

=item B<create_user>

Создание пользователя

=back

=cut
