using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using RabbitMQ.Client;
using RabbitMQ.Client.MessagePatterns;
using RabbitMQ.Client.Events;
using System.Threading;

namespace Recipe01
{




    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {


        public MainWindow()
        {
            InitializeComponent();
        }

        ConnectionFactory connection_factory = new ConnectionFactory();
        IConnection conn = null;
        IModel channel = null;
        Subscription sub = null;
        private String news_exchange = "myLastnews.fanout_01/06";
        private void Connect()
        {
            try
            {
                Disconnet();
                connection_factory.Uri = "amqp://guest:guest@" + edRabbitmqHost.Text + ":5672/";
                conn = connection_factory.CreateConnection();
                channel = conn.CreateModel();
                lsnews.Items.Add("------Connetected!");

                String myqueue = channel.QueueDeclare().QueueName;
                channel.QueueBind(myqueue,news_exchange, "");
                sub = new Subscription(channel, myqueue, true);
                StartSubscriberThread(sub);
                lsnews.Items.Add("------Subscriber started");
                Connected = true;
            }
            catch (Exception e)
            {
                lsnews.Items.Add("ERROR:" + e.Message);

            }
        }


        public Thread StartSubscriberThread(Subscription sub)
        {
            var t = new Thread(() => InternalStartSubscriber(sub));
            t.Start();
            return t;
        }

        private void InternalStartSubscriber(Subscription sub)
        {

            foreach (BasicDeliverEventArgs e in sub)
            {
                Action<String> action = delegate(String value)
                {
                    lsnews.Items.Insert(0,value);
                    if (lsnews.Items.Count >= 50) {
                        lsnews.Items.RemoveAt(lsnews.Items.Count-1);
                    }
                };
                Dispatcher.BeginInvoke(action, Encoding.UTF8.GetString(e.Body));
            }


        }






        public bool Connected
        {
            get { return (bool)GetValue(ConnectedProperty); }
            set { SetValue(ConnectedProperty, value); }
        }

        // Using a DependencyProperty as the backing store for Connected.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty ConnectedProperty =
            DependencyProperty.Register("Connected", typeof(bool), typeof(MainWindow), new UIPropertyMetadata(false));




        private void Disconnet()
        {

            if (sub != null)
            {
                sub.Close();
                
                sub = null;
            }
           
            if (channel != null)
            {
                channel.Close();
                channel.Dispose();
                channel = null;
            }
            if (conn != null)
            {

                conn.Close();
                conn.Dispose();
                conn = null;
            }
      

            Connected = false;


        }
        private void btnConnect_Click(object sender, RoutedEventArgs e)
        {
            Connect();
        }

        private void btnDisconnect_Click(object sender, RoutedEventArgs e)
        {
            Disconnet();
            lsnews.Items.Add("------Disconnected.");
        }

        private void myself_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            Disconnet();
        }

        
    }


    [ValueConversion(typeof(bool), typeof(bool))]
    public class InverseBooleanConverter : IValueConverter
    {
        #region IValueConverter Members

        public object Convert(object value, Type targetType, object parameter,
            System.Globalization.CultureInfo culture)
        {
            if (targetType != typeof(bool))
                throw new InvalidOperationException("The target must be a boolean");

            return !(bool)value;
        }

        public object ConvertBack(object value, Type targetType, object parameter,
            System.Globalization.CultureInfo culture)
        {
            throw new NotSupportedException();
        }

        #endregion
    }
}
