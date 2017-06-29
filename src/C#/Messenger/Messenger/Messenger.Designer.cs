namespace Messenger
{
    partial class Messenger
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btLogin = new System.Windows.Forms.Button();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.lMessenger = new System.Windows.Forms.Label();
            this.pictureBox1 = new System.Windows.Forms.PictureBox();
            this.lName = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // btLogin
            // 
            this.btLogin.Font = new System.Drawing.Font("Microsoft Sans Serif", 16F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btLogin.Location = new System.Drawing.Point(53, 300);
            this.btLogin.Name = "btLogin";
            this.btLogin.Size = new System.Drawing.Size(152, 44);
            this.btLogin.TabIndex = 0;
            this.btLogin.Text = "Login";
            this.btLogin.UseMnemonic = false;
            this.btLogin.UseVisualStyleBackColor = true;
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(74, 251);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(106, 20);
            this.textBox1.TabIndex = 1;
            // 
            // lMessenger
            // 
            this.lMessenger.Font = new System.Drawing.Font("Microsoft Sans Serif", 20F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lMessenger.Location = new System.Drawing.Point(53, 25);
            this.lMessenger.Name = "lMessenger";
            this.lMessenger.Size = new System.Drawing.Size(152, 41);
            this.lMessenger.TabIndex = 2;
            this.lMessenger.Text = "Messenger";
            this.lMessenger.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // pictureBox1
            // 
            this.pictureBox1.ImageLocation = "D:\\GitProjects\\Messenger\\src\\C#\\Messenger\\icon.png";
            this.pictureBox1.Location = new System.Drawing.Point(78, 95);
            this.pictureBox1.Name = "pictureBox1";
            this.pictureBox1.Size = new System.Drawing.Size(102, 93);
            this.pictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage;
            this.pictureBox1.TabIndex = 3;
            this.pictureBox1.TabStop = false;
            // 
            // lName
            // 
            this.lName.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lName.Location = new System.Drawing.Point(75, 210);
            this.lName.Name = "lName";
            this.lName.Size = new System.Drawing.Size(105, 28);
            this.lName.TabIndex = 4;
            this.lName.Text = "Enter a Name:";
            this.lName.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // Messenger
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(257, 440);
            this.Controls.Add(this.lName);
            this.Controls.Add(this.pictureBox1);
            this.Controls.Add(this.lMessenger);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.btLogin);
            this.Name = "Messenger";
            this.Text = "Messenger";
            ((System.ComponentModel.ISupportInitialize)(this.pictureBox1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button btLogin;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Label lMessenger;
        private System.Windows.Forms.PictureBox pictureBox1;
        private System.Windows.Forms.Label lName;
    }
}

